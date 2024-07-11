package test.Selsup.CrptApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CrptAPI {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;
    private final Gson gson;
    private final Lock lock;

    public CrptAPI(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.semaphore = new Semaphore(requestLimit);
        this.gson = new GsonBuilder().create();
        this.lock = new ReentrantLock();

        long interval = timeUnit.toMillis(1);
        scheduler.scheduleAtFixedRate(semaphore::release, 0, interval, TimeUnit.MILLISECONDS);
    }

    public void createDocument(Document document) throws InterruptedException, IOException {
        semaphore.acquire();

        lock.lock();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://ismp.crpt.ru/api/v3/lk/documents/create");
            post.setHeader("Content-Type", "application/json");

            String json = gson.toJson(document);
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = httpClient.execute(post)) {
                System.out.println(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
            }
        } finally {
            lock.unlock();
        }
    }

    public static class Document {
        public Description description;
        public String doc_id;
        public String doc_status;
        public String doc_type;
        public boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;
        public String production_date;
        public String production_type;
        public Product[] products;
        public String reg_date;
        public String reg_number;


        public static class Description {
            public String participantInn;
        }

        public static class Product {
            public String certificate_document;
            public String certificate_document_date;
            public String certificate_document_number;
            public String owner_inn;
            public String producer_inn;
            public String production_date;
            public String tnved_code;
            public String uit_code;
            public String uitu_code;
        }
    }
}