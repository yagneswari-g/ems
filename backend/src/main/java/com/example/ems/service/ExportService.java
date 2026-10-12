package com.example.ems.service;

import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ExportService {
    private final EmployeeRepository employees; private final AuthService auth;
    public ExportService(EmployeeRepository employees, AuthService auth) { this.employees = employees; this.auth = auth; }

    public byte[] csv(String token) {
        auth.requireManager(token); StringBuilder out = new StringBuilder("ID,Name,Email,Phone,Department,Designation,Salary,Status\n");
        employees.findAll().forEach(e -> out.append(csv(e.getId())).append(',').append(csv(e.getFirstName() + " " + e.getLastName())).append(',').append(csv(e.getEmail())).append(',').append(csv(e.getPhone())).append(',').append(csv(e.getDepartment() == null ? "Unassigned" : e.getDepartment().getName())).append(',').append(csv(e.getDesignation())).append(',').append(e.getSalary() == null ? "" : e.getSalary()).append(',').append(e.getStatus()).append('\n'));
        return out.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] pdf(String token) {
        auth.requireManager(token); List<Employee> list = employees.findAll(); ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            Document document = new Document(PageSize.A4.rotate()); PdfWriter.getInstance(document, output); document.open();
            document.add(new Paragraph("EmployeeHub - Employee Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph("Generated employee list")); document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(8); table.setWidthPercentage(100);
            String[] headers = {"ID", "Name", "Email", "Phone", "Department", "Designation", "Salary", "Status"};
            for (String h : headers) table.addCell(new Phrase(h));
            for (Employee e : list) { table.addCell(String.valueOf(e.getId())); table.addCell(e.getFirstName() + " " + e.getLastName()); table.addCell(safe(e.getEmail())); table.addCell(safe(e.getPhone())); table.addCell(e.getDepartment() == null ? "Unassigned" : e.getDepartment().getName()); table.addCell(safe(e.getDesignation())); table.addCell(e.getSalary() == null ? "" : e.getSalary().toString()); table.addCell(e.getStatus().toString()); }
            document.add(table); document.close(); return output.toByteArray();
        } catch (Exception ex) { throw new IllegalStateException("Could not create PDF", ex); }
    }
    private String csv(Object value) { String s = value == null ? "" : String.valueOf(value); return "\"" + s.replace("\"", "\"\"") + "\""; }
    private String safe(String value) { return value == null ? "" : value; }
}
