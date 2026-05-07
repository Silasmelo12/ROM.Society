package concept.com.example.club.core.hardware.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DeduplicacaoService {

    private final Cache<String, AtomicInteger> cache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();

    public boolean isDuplicada(String id){
        AtomicInteger contador = cache.get(id, k -> new AtomicInteger(0));

        int quantidadeDeRequisicoes = contador.incrementAndGet();

        return quantidadeDeRequisicoes > 1;


    }
}
