package com.coffee.dropbox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;

/**
 * @author Aman Kumar Seth
 */
@Slf4j
@Service
public class DropboxService {

    @Getter
    private DbxClientV2 client;

    @Value("${dropbox.accessToken}")
    private String accessToken;

    @Value("${dropbox.appKey}")
    private String appKey;

    @Value("${dropbox.appSecret}")
    private String appSecret;

    @Value("${dropbox.refreshToken}")
    private String refreshToken;

    @PostConstruct
    public void init() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("spring-boot-dropbox-app").build();
//        client = new DbxClientV2(config, accessToken);

        // DbxCredential automatically refreshes short-lived access tokens
        DbxCredential credential = new DbxCredential(
                accessToken,           // initial access token (leave blank if unknown)
                -1L,          // expiresAt (not needed if unknown)
                refreshToken, // your refresh token
                appKey,
                appSecret
        );

        client = new DbxClientV2(config, credential);
    }

    public void uploadImage(MultipartFile file, String dropboxPath) throws Exception {

        try (InputStream in = file.getInputStream()) {

            log.debug("[DropboxService][uploadImage] Uploading file to Dropbox: {}", dropboxPath);

            FileMetadata metadata = client.files()
                    .uploadBuilder(dropboxPath)
                    .withMode(WriteMode.OVERWRITE) // ✅ ensures file overwrite
                    .uploadAndFinish(in);

            log.debug("[DropboxService][uploadImage] Uploaded file to Dropbox: {}", metadata.getPathLower());

            metadata.getPathLower();
        }
    }

    public String getSharableLink(String dropboxPath) throws Exception {

//        SharedLinkMetadata sharedLink = client.sharing()
//                .createSharedLinkWithSettings(dropboxPath);
//        log.debug("[DropboxService][getSharableLink] Shared link: {}", sharedLink.getUrl());
//        return sharedLink.getUrl().replace("&dl=0", "&raw=1"); // direct view link

        try {
            // Try to create a new shared link
            SharedLinkMetadata sharedLink = client.sharing()
                    .createSharedLinkWithSettings(dropboxPath);

            log.debug("[DropboxService][getSharableLink] Created new shared link: {}", sharedLink.getUrl());

            return sharedLink.getUrl().replace("&dl=0", "&raw=1");

        } catch (com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException e) {

            // If link already exists, fetch existing links
            if (e.errorValue.isSharedLinkAlreadyExists()) {
                ListSharedLinksResult existingLinks = client.sharing()
                        .listSharedLinksBuilder()
                        .withPath(dropboxPath)
                        .withDirectOnly(true)
                        .start();

                if (!existingLinks.getLinks().isEmpty()) {
                    String existingUrl = existingLinks.getLinks().get(0).getUrl();
                    log.debug("[DropboxService][getSharableLink] Existing shared link found: {}", existingUrl);
                    return existingUrl.replace("&dl=0", "&raw=1");
                }
            }
            throw e; // rethrow if it’s some other error
        }

    }
}
