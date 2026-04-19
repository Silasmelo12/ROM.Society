package concept.com.example.club.service;

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
        // O método "get" do Caffeine faz o seguinte:
        // Pega o contador do ID. Se não existir, cria um novo começando em zero.
        AtomicInteger contador = cache.get(id, k -> new AtomicInteger(0));

        // Adiciona +1 ao contador e pega o resultado
        int quantidadeDeRequisicoes = contador.incrementAndGet();

        // Limite estabelecido: Se a requisição for a 3ª, 4ª, 13ª... bloqueia.
        return quantidadeDeRequisicoes > 1; // É duplicada (bloqueia)

        // Se for a 1ª ou a 2ª, deixa passar!

    }
}
