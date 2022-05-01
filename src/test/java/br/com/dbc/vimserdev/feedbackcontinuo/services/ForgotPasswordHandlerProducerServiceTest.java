package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ForgotPasswordHandlerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ForgotPasswordHandlerProducerServiceTest {

    @InjectMocks
    private ForgotPasswordHandlerProducerService forgotPasswordHandlerProducerService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void shouldCallSendPasswordNew() throws JsonProcessingException {

        when(objectMapper.writeValueAsString(any(ForgotPasswordHandlerDTO.class))).thenReturn("senha:romeu");
        when(kafkaTemplate.send(any(Message.class))).thenReturn(getListenable());

        forgotPasswordHandlerProducerService.send(ForgotPasswordHandlerDTO.builder().build());

        verify(kafkaTemplate,times(1)).send(any(Message.class));

    }

    private ListenableFuture<SendResult<String, String>> getListenable(){

        return new ListenableFuture<>() {
            @Override
            public void addCallback(ListenableFutureCallback<? super SendResult<String, String>> callback) {

            }

            @Override
            public void addCallback(SuccessCallback<? super SendResult<String, String>> successCallback, FailureCallback failureCallback) {

            }

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public SendResult<String, String> get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public SendResult<String, String> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }
}
