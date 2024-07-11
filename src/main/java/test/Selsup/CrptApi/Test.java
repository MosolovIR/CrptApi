package test.Selsup.CrptApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        try {
            CrptAPI crptAPI = new CrptAPI(TimeUnit.SECONDS, 5);
            CrptAPI.Document document = new CrptAPI.Document();
            document.description = new CrptAPI.Document.Description();
            document.description.participantInn = "123456789";
            document.doc_id = "123456789";
            document.doc_status = "Active";
            document.doc_type = "products";
            document.importRequest = true;
            document.owner_inn = "123456789";
            document.participant_inn = "123456789";
            document.producer_inn = "123456789";
            document.production_date = "2024-07-11";
            document.production_type = "production_type";

            document.products = new CrptAPI.Document.Product[1];
            CrptAPI.Document.Product product = new CrptAPI.Document.Product();
            product.certificate_document = "certificate_document";
            product.certificate_document_date = "2024-07-11";
            product.certificate_document_number = "123456789";
            product.owner_inn = "123456789";
            product.producer_inn = "123456789";
            product.production_date = "2024-07-11";
            product.tnved_code = "123456789";
            product.uit_code = "123456789";
            product.uitu_code = "123456789";
            document.products[0] = product;
            document.reg_date = "2024-07-11";
            document.reg_number = "123456789";

            crptAPI.createDocument(document);

            System.out.println("Document created");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
