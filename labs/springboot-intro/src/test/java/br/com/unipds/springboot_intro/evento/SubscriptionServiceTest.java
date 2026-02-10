package br.com.unipds.springboot_intro.evento;

import br.com.unipds.springboot_intro.model.evento.Subscription;
import br.com.unipds.springboot_intro.model.evento.repository.SubscriptionRepository;
import br.com.unipds.springboot_intro.model.evento.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService service;

    @Mock
    private SubscriptionRepository repository;

    @Test
    public void deveGerarUuidEDataAoSalvar() {
        // Cenário: Inscrição chegando sem ID único e sem data
        Subscription novaInscricao = new Subscription();
        novaInscricao.setLevel(2);

        // Mock do repositório para devolver o próprio objeto que foi salvo
        when(repository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execução
        Subscription salvo = service.addSubscription(novaInscricao);

        // Validações
        assertNotNull(salvo.getCreatedAt(), "A data de criação não deve ser nula");
        assertNotNull(salvo.getUniqueId(), "O UniqueID deve ser gerado automaticamente");
        assertEquals(2, salvo.getLevel());

        // Verifica se o método save do repositório foi chamado
        verify(repository).save(novaInscricao);
    }
}