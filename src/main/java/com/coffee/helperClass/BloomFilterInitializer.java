package com.coffee.helperClass;

import com.coffee.repository.UsersRepository;
import com.google.common.hash.BloomFilter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BloomFilterInitializer {

    private final BloomFilter<String> usernameBloomFilter;
    private final UsersRepository userRepository;

    @PostConstruct
    public void initialize() {
        List<String> allUsernames = userRepository.findAllUsernames();
        allUsernames.forEach(usernameBloomFilter::put);
    }
}
