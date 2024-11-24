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

public class Doctor {
    private String id;
    private String name;
    private String specialization;
    private String contact;

    public Doctor(String id, String name, String specialization, String contact) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    // Save a new doctor to the XML file
    public void save() throws Exception {
        File file = new File("doctors.xml");
        Document document;
        Element root;

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.parse(file);
            root = document.getDocumentElement();
        } else {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.newDocument();
            root = document.createElement("Doctors");
            document.appendChild(root);
        }

        Element doctor = document.createElement("Doctor");

        Element idElement = document.createElement("Id");
        idElement.appendChild(document.createTextNode(id));
        doctor.appendChild(idElement);

        Element nameElement = document.createElement("Name");
        nameElement.appendChild(document.createTextNode(name));
        doctor.appendChild(nameElement);

        Element specializationElement = document.createElement("Specialization");
        specializationElement.appendChild(document.createTextNode(specialization));
        doctor.appendChild(specializationElement);

        Element contactElement = document.createElement("Contact");
        contactElement.appendChild(document.createTextNode(contact));
        doctor.appendChild(contactElement);

        root.appendChild(doctor);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    // Fetch all doctors
    public static List<Doctor> fetchAll() throws Exception {
        File file = new File("doctors.xml");
        List<Doctor> doctors = new ArrayList<>();

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Doctor");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element doctorElement = (Element) nodes.item(i);

                String id = doctorElement.getElementsByTagName("Id").item(0).getTextContent();
                String name = doctorElement.getElementsByTagName("Name").item(0).getTextContent();
                String specialization = doctorElement.getElementsByTagName("Specialization").item(0).getTextContent();
                String contact = doctorElement.getElementsByTagName("Contact").item(0).getTextContent();

                doctors.add(new Doctor(id, name, specialization, contact));
            }
        }

        return doctors;
    }

    // Find a doctor by ID
    public static Doctor findById(String id) throws Exception {
        File file = new File("doctors.xml");

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Doctor");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element doctorElement = (Element) nodes.item(i);
                String doctorId = doctorElement.getElementsByTagName("Id").item(0).getTextContent();

                if (doctorId.equals(id)) {
                    String name = doctorElement.getElementsByTagName("Name").item(0).getTextContent();
                    String specialization = doctorElement.getElementsByTagName("Specialization").item(0).getTextContent();
                    String contact = doctorElement.getElementsByTagName("Contact").item(0).getTextContent();

                    return new Doctor(id, name, specialization, contact);
                }
            }
        }

        return null;
    }
}
