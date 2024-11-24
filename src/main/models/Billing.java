package main.models;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Billing {
    private String id;
    private String patientId;
    private List<String> services;
    private double totalAmount;

    public Billing(String id, String patientId, List<String> services, double totalAmount) {
        this.id = id;
        this.patientId = patientId;
        this.services = services;
        this.totalAmount = totalAmount;
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Save a new billing record to the XML file
    public void save() throws Exception {
        File file = new File("billing.xml");
        Document document;
        Element root;

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.parse(file);
            root = document.getDocumentElement();
        } else {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.newDocument();
            root = document.createElement("Billings");
            document.appendChild(root);
        }

        Element billing = document.createElement("Billing");

        Element idElement = document.createElement("Id");
        idElement.appendChild(document.createTextNode(id));
        billing.appendChild(idElement);

        Element patientIdElement = document.createElement("PatientId");
        patientIdElement.appendChild(document.createTextNode(patientId));
        billing.appendChild(patientIdElement);

        Element servicesElement = document.createElement("Services");
        for (String service : services) {
            Element serviceElement = document.createElement("Service");
            serviceElement.appendChild(document.createTextNode(service));
            servicesElement.appendChild(serviceElement);
        }
        billing.appendChild(servicesElement);

        Element totalAmountElement = document.createElement("TotalAmount");
        totalAmountElement.appendChild(document.createTextNode(String.valueOf(totalAmount)));
        billing.appendChild(totalAmountElement);

        root.appendChild(billing);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    // Fetch all billing records
    public static List<Billing> fetchAll() throws Exception {
        File file = new File("billing.xml");
        List<Billing> billings = new ArrayList<>();

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Billing");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element billingElement = (Element) nodes.item(i);

                String id = billingElement.getElementsByTagName("Id").item(0).getTextContent();
                String patientId = billingElement.getElementsByTagName("PatientId").item(0).getTextContent();

                List<String> services = new ArrayList<>();
                NodeList serviceNodes = billingElement.getElementsByTagName("Service");
                for (int j = 0; j < serviceNodes.getLength(); j++) {
                    services.add(serviceNodes.item(j).getTextContent());
                }

                double totalAmount = Double.parseDouble(billingElement.getElementsByTagName("TotalAmount").item(0).getTextContent());

                billings.add(new Billing(id, patientId, services, totalAmount));
            }
        }

        return billings;
    }

    // Find billing records by patient ID
    public static List<Billing> findByPatientId(String patientId) throws Exception {
        File file = new File("billing.xml");
        List<Billing> billings = new ArrayList<>();

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Billing");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element billingElement = (Element) nodes.item(i);
                String id = billingElement.getElementsByTagName("PatientId").item(0).getTextContent();

                if (id.equals(patientId)) {
                    String billingId = billingElement.getElementsByTagName("Id").item(0).getTextContent();
                    List<String> services = new ArrayList<>();
                    NodeList serviceNodes = billingElement.getElementsByTagName("Service");
                    for (int j = 0; j < serviceNodes.getLength(); j++) {
                        services.add(serviceNodes.item(j).getTextContent());
                    }
                    double totalAmount = Double.parseDouble(billingElement.getElementsByTagName("TotalAmount").item(0).getTextContent());

                    billings.add(new Billing(billingId, patientId, services, totalAmount));
                }
            }
        }

        return billings;
    }
}
