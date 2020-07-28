package com.example.demo.service;

import com.example.demo.domain.Link;
import com.example.demo.domain.Metadata;
import com.example.demo.domain.Trackpoint;
import com.example.demo.domain.Waypoint;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.MyFileNotFoundException;
import com.example.demo.property.FileStorageProperties;
import com.example.demo.repositories.LinkRepository;
import com.example.demo.repositories.MetadataRepository;
import com.example.demo.repositories.TrackpointRepository;
import com.example.demo.repositories.WaypointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private WaypointRepository waypointRepository;

    @Autowired
    private TrackpointRepository trackpointRepository;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            parseFile(targetLocation);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private void parseFile(Path filePath) {
        System.out.println(filePath);
        readFile(filePath);
    }

    private void readFile(Path filePath) {
        try {
            File f = new File(filePath.toString());
            System.setProperty("FilePath", f.getAbsolutePath());
            System.out.println(f.getAbsolutePath());
            parseXML(f);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void parseXML(File f) throws SAXException, ParserConfigurationException, IOException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(f);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList metadataNode = doc.getElementsByTagName("metadata");
        setDataForObjectMetadata(metadataNode);
        NodeList wptNode = doc.getElementsByTagName("wpt");
        setDataForObjectWaypoint(wptNode);
        NodeList trkptNode = doc.getElementsByTagName("trkpt");
        setDataForObjectTrackpoint(trkptNode);
    }

    private void setDataForObjectTrackpoint(NodeList trkptNode) {
        List<Trackpoint> trackpointList = new ArrayList<>();
        for(int i=0; i < trkptNode.getLength(); i++){
            Trackpoint trkpt = new Trackpoint();
            Node n = trkptNode.item(i);
            if(n.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)n;
                trkpt.setTrkptElement(getTagValue("ele", element));
                trkpt.setTrkptDateTime(getTagValue("time", element));
                trkpt.setTrkptLatitude(element.getAttribute("lat"));
                trkpt.setTrkptLontitude(element.getAttribute("lon"));
            }
            System.out.println(trkpt);
            trackpointList.add(trkpt);
            trackpointRepository.save(trkpt);
        }
    }

    private void setDataForObjectWaypoint(NodeList wptNode) {
        List<Waypoint> wptElements = new ArrayList<>();
        for(int i=0; i < wptNode.getLength(); i++){
            Waypoint wpt = new Waypoint();
            Node n = wptNode.item(i);
            if(n.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)n;
                wpt.setName(getTagValue("name", element));
                wpt.setSym(getTagValue("sym", element));
                wpt.setLatitude(element.getAttribute("lat"));
                wpt.setLongitude(element.getAttribute("lon"));
            }
            System.out.println(wpt);
            wptElements.add(wpt);
            waypointRepository.save(wpt);
        }
    }

    private void setDataForObjectMetadata(NodeList metadataNode) {
        List<Metadata> metadataElements = new ArrayList<>();
        for(int i=0; i < metadataNode.getLength(); i++) {
            Metadata metadata = new Metadata();
            Node node =  metadataNode.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                metadata.setName(getTagValue("name", element));
                metadata.setDescription(getTagValue("desc", element));
                metadata.setAuthor(getTagValue("author", element));
                metadata.setTime(getTagValue("time", element));
                Element childElement = getDirectChild(element, "link");
                String href = childElement.getAttribute("href");
                Link l = new Link();
                l.setText(getTagValue("text", childElement));
                l.setUrl(href);
                System.out.println(l);
                linkRepository.save(l);
            }
            System.out.println(metadata);
            metadataRepository.save(metadata);

            metadataElements.add(metadata);
        }
    }

    private static Element getDirectChild(Element parent, String name)
    {
        for(Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if(child instanceof Element && name.equals(child.getNodeName())) return (Element) child;
        }
        return null;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        if(Objects.isNull(node)){
            return " ";
        }
        return node.getNodeValue();
    }

}

