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

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private String date;
    private String time;

    public Appointment(String id, String patientId, String doctorId, String date, String time) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Save a new appointment to the XML file
    public void save() throws Exception {
        File file = new File("appointments.xml");
        Document document;
        Element root;

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.parse(file);
            root = document.getDocumentElement();
        } else {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.newDocument();
            root = document.createElement("Appointments");
            document.appendChild(root);
        }

        Element appointment = document.createElement("Appointment");

        Element idElement = document.createElement("Id");
        idElement.appendChild(document.createTextNode(id));
        appointment.appendChild(idElement);

        Element patientIdElement = document.createElement("PatientId");
        patientIdElement.appendChild(document.createTextNode(patientId));
        appointment.appendChild(patientIdElement);

        Element doctorIdElement = document.createElement("DoctorId");
        doctorIdElement.appendChild(document.createTextNode(doctorId));
        appointment.appendChild(doctorIdElement);

        Element dateElement = document.createElement("Date");
        dateElement.appendChild(document.createTextNode(date));
        appointment.appendChild(dateElement);

        Element timeElement = document.createElement("Time");
        timeElement.appendChild(document.createTextNode(time));
        appointment.appendChild(timeElement);

        root.appendChild(appointment);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    // Fetch all appointments
    public static List<Appointment> fetchAll() throws Exception {
        File file = new File("appointments.xml");
        List<Appointment> appointments = new ArrayList<>();

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Appointment");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element appointmentElement = (Element) nodes.item(i);

                String id = appointmentElement.getElementsByTagName("Id").item(0).getTextContent();
                String patientId = appointmentElement.getElementsByTagName("PatientId").item(0).getTextContent();
                String doctorId = appointmentElement.getElementsByTagName("DoctorId").item(0).getTextContent();
                String date = appointmentElement.getElementsByTagName("Date").item(0).getTextContent();
                String time = appointmentElement.getElementsByTagName("Time").item(0).getTextContent();

                appointments.add(new Appointment(id, patientId, doctorId, date, time));
            }
        }

        return appointments;
    }

    // Find appointments by patient ID
    public static List<Appointment> findByPatientId(String patientId) throws Exception {
        File file = new File("appointments.xml");
        List<Appointment> appointments = new ArrayList<>();

        if (file.exists()) {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = dBuilder.parse(file);
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Appointment");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element appointmentElement = (Element) nodes.item(i);
                String id = appointmentElement.getElementsByTagName("PatientId").item(0).getTextContent();

                if (id.equals(patientId)) {
                    String appointmentId = appointmentElement.getElementsByTagName("Id").item(0).getTextContent();
                    String doctorId = appointmentElement.getElementsByTagName("DoctorId").item(0).getTextContent();
                    String date = appointmentElement.getElementsByTagName("Date").item(0).getTextContent();
                    String time = appointmentElement.getElementsByTagName("Time").item(0).getTextContent();

                    appointments.add(new Appointment(appointmentId, patientId, doctorId, date, time));
                }
            }
        }

        return appointments;
    }
}
