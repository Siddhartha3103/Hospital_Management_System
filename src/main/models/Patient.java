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

public class Patient {
    private String id;
    private String name;
    private int age;
    private String contact;

    public Patient(String id, String name, int age, String contact) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // Save a new patient to the XML file
    public void save() throws Exception {
        File file = new File("patients.xml");
        Document document;
        Element root;

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.parse(file);
            root = document.getDocumentElement();
        } else {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.newDocument();
            root = document.createElement("Patients");
            document.appendChild(root);
        }

        Element patient = document.createElement("Patient");

        Element idElement = document.createElement("Id");
        idElement.appendChild(document.createTextNode(id));
        patient.appendChild(idElement);

        Element nameElement = document.createElement("Name");
        nameElement.appendChild(document.createTextNode(name));
        patient.appendChild(nameElement);

        Element ageElement = document.createElement("Age");
        ageElement.appendChild(document.createTextNode(String.valueOf(age)));
        patient.appendChild(ageElement);

        Element contactElement = document.createElement("Contact");
        contactElement.appendChild(document.createTextNode(contact));
        patient.appendChild(contactElement);

        root.appendChild(patient);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    // Fetch all patients
    public static List<Patient> fetchAll() throws Exception {
        File file = new File("patients.xml");
        List<Patient> patients = new ArrayList<>();

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Patient");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element patientElement = (Element) nodes.item(i);

                String id = patientElement.getElementsByTagName("Id").item(0).getTextContent();
                String name = patientElement.getElementsByTagName("Name").item(0).getTextContent();
                int age = Integer.parseInt(patientElement.getElementsByTagName("Age").item(0).getTextContent());
                String contact = patientElement.getElementsByTagName("Contact").item(0).getTextContent();

                patients.add(new Patient(id, name, age, contact));
            }
        }

        return patients;
    }

    // Find a patient by ID
    public static Patient findById(String id) throws Exception {
        File file = new File("patients.xml");

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Patient");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element patientElement = (Element) nodes.item(i);
                String patientId = patientElement.getElementsByTagName("Id").item(0).getTextContent();

                if (patientId.equals(id)) {
                    String name = patientElement.getElementsByTagName("Name").item(0).getTextContent();
                    int age = Integer.parseInt(patientElement.getElementsByTagName("Age").item(0).getTextContent());
                    String contact = patientElement.getElementsByTagName("Contact").item(0).getTextContent();

                    return new Patient(id, name, age, contact);
                }
            }
        }

        return null;
    }
}
