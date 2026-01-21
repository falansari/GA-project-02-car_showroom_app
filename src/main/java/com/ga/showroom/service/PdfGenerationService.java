package com.ga.showroom.service;

import com.ga.showroom.exception.AccessDeniedException;
import com.ga.showroom.exception.InformationNotFoundException;
import com.ga.showroom.model.*;
import com.ga.showroom.model.enums.Role;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import static com.ga.showroom.service.UserService.getCurrentLoggedInUser;

@Service
public class PdfGenerationService {

    private final JavaMailSender mailSender;
    private final OrderService orderService;
    private final Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
    private final Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
    private final Font textFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
    Color mainColor = new Color(190, 220, 216);

    public PdfGenerationService(JavaMailSender mailSender,  OrderService orderService) {

        this.mailSender = mailSender;
        this.orderService = orderService;
    }

    PdfPCell headerInline(String input) {
        PdfPCell cell = new PdfPCell(new Phrase(input, headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(15);
        return cell;
    }

    PdfPCell dataInline(String input) {
        PdfPCell cell = new PdfPCell(new Phrase(input, textFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(15);
        return cell;
    }

    PdfPCell header(String input) {
        PdfPCell cell = new PdfPCell(new Phrase(input, headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(mainColor);
        cell.setBorderColor(Color.DARK_GRAY);
        cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        cell.setFixedHeight(25);
        return cell;
    }


    PdfPCell data(String input) {
        PdfPCell cell = new PdfPCell(new Phrase(input, textFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(18);
        return cell;
    }

    PdfPCell dataRight(String input) {
        PdfPCell cell = new PdfPCell(new Phrase(input, textFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(18);
        return cell;
    }

    PdfPCell totalData(String input) {
        PdfPCell cell = new PdfPCell(new Phrase(input, headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(new Color(236, 236, 236));
        cell.setBorderColor(Color.DARK_GRAY);
        cell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
        cell.setFixedHeight(18);
        return cell;
    }

    public String generateOrderReceipt(Long orderId) throws MessagingException {
        if(!getCurrentLoggedInUser().getRole().equals(Role.ADMIN) && !getCurrentLoggedInUser().getRole().equals(Role.SALESMAN))
            throw new AccessDeniedException("You are not authorized to send order receipt. Please contact a salesman or admin.");

        Order order = orderService.getById(orderId);
        if (order == null) throw new InformationNotFoundException("Order with ID" + orderId + " does not exist");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 48, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            /////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////                   header                ////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(90);
            headerTable.setSpacingBefore(3);
            headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.setWidths(new float[]{65, 25}); // left / right ratio

            // LEFT: Title
            PdfPCell leftCell = new PdfPCell(
                    new Phrase("Order Receipt", titleFont)
            );
            leftCell.setBorderColor(Color.DARK_GRAY);
            leftCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            leftCell.setBackgroundColor(mainColor);
            leftCell.setFixedHeight(60);
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            leftCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            leftCell.setPadding(10);

            // RIGHT: Date & time
            PdfPCell rightCell = new PdfPCell(
                    new Phrase(
                            order.getCreatedAt().toLocalDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + "\n" +
                                    order.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")),
                            textFont
                    )
            );
            rightCell.setBorderColor(Color.DARK_GRAY);
            rightCell.setBorder(Rectangle.TOP | Rectangle.BOTTOM);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.setBackgroundColor(mainColor);
            rightCell.setFixedHeight(60);
            rightCell.setPadding(10);

            headerTable.addCell(leftCell);
            headerTable.addCell(rightCell);
            headerTable.setSpacingAfter(30f);

            document.add(headerTable);

            /////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////              Owner & Car Info         /////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////
            PdfPTable InfoTable = new PdfPTable(2);
            InfoTable.setWidthPercentage(90);
            InfoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            InfoTable.setWidths(new float[]{45, 45});

            PdfPTable ownerTable = new PdfPTable(2);
            ownerTable.setWidthPercentage(100);
            ownerTable.setWidths(new float[]{30, 70});

            ownerTable.addCell(headerInline("Name"));
            ownerTable.addCell(dataInline(order.getCustomer().getUserProfile().getFirstName() +" " + order.getCustomer().getUserProfile().getLastName()));

            ownerTable.addCell(headerInline("Email"));
            ownerTable.addCell(dataInline(order.getCustomer().getEmailAddress()));

            ownerTable.addCell(headerInline("phone"));
            ownerTable.addCell(dataInline(order.getCustomer().getUserProfile().getPhoneNumber().toString()));

            ownerTable.addCell(headerInline("Address"));
            ownerTable.addCell(dataInline(order.getCustomer().getUserProfile().getHomeAddress()));

            ownerTable.addCell(headerInline("CPR"));
            ownerTable.addCell(dataInline(order.getCustomer().getUserProfile().getCpr().toString()));


            PdfPTable carTable = new PdfPTable(2);
            carTable.setWidthPercentage(100);
            carTable.setWidths(new float[]{40, 60});

            carTable.addCell(headerInline("Car model"));
            carTable.addCell(dataInline(order.getCar().getCarModel().getName() + " " + order.getCar().getCarModel().getMakeYear()));

            carTable.addCell(headerInline("Manufacturer"));
            carTable.addCell(dataInline(order.getCar().getCarModel().getManufacturer()));

            carTable.addCell(headerInline("Vehicle number"));
            carTable.addCell(dataInline(order.getCar().getVinNumber()));

            carTable.addCell(headerInline("Registration number"));
            carTable.addCell(dataInline(order.getCar().getRegistrationNumber()));

            carTable.addCell(headerInline("Insurance policy"));
            carTable.addCell(dataInline(order.getCar().getInsurancePolicy()));

            PdfPCell leftInfoCell = new PdfPCell(ownerTable);
            leftInfoCell.setBorder(Rectangle.NO_BORDER);

            PdfPCell rightInfoCell = new PdfPCell(carTable);
            rightInfoCell.setBorder(Rectangle.NO_BORDER);

            InfoTable.addCell(headerInline("Owner Details"));
            InfoTable.addCell(headerInline("Car Details"));
            InfoTable.addCell(leftInfoCell);
            InfoTable.addCell(rightInfoCell);
            InfoTable.setSpacingAfter(30f);


            document.add(InfoTable);

            /////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////              Options & Price Details         /////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////

            PdfPTable DetailsTable = new PdfPTable(3);
            DetailsTable.setWidthPercentage(90);
            DetailsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            DetailsTable.setWidths(new float[]{30, 30, 30});

            DetailsTable.addCell(header("Category"));
            DetailsTable.addCell(header("Choice"));
            DetailsTable.addCell(header("price"));

            DetailsTable.addCell(data("Original Car price"));
            DetailsTable.addCell(data(""));
            DetailsTable.addCell(dataRight(order.getCar().getCarModel().getPrice().toString()));

            for(CarOption carOption: order.getCar().getCarOptions()){
                DetailsTable.addCell(data(carOption.getOption().getOptionCategory().getName()));
                DetailsTable.addCell(data(carOption.getOption().getName()));
                DetailsTable.addCell(dataRight(carOption.getOption().getPrice().toString()));
            }

            DetailsTable.addCell(totalData(""));
            DetailsTable.addCell(totalData("Total Price"));
            DetailsTable.addCell(totalData(order.getTotalPrice().toString()));

            document.add(DetailsTable);


        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        byte[] pdfBytes = out.toByteArray();

        // Send email with PDF attachment
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(order.getCustomer().getEmailAddress());
        helper.setFrom("no-reply@showroom.com");
        helper.setSubject("Showroom Receipt");
        helper.setText(
                "Dear " + order.getCustomer().getUserProfile().getFirstName() + " " + order.getCustomer().getUserProfile().getLastName()+",\n\nPlease find attached your receipt.\n\nRegards,\nShowroom Team"
        );

        helper.addAttachment(
                "order-receipt.pdf",
                new ByteArrayResource(pdfBytes)
        );

        mailSender.send(message);
        return "Email sent to " + order.getCustomer().getEmailAddress();
    }
}
