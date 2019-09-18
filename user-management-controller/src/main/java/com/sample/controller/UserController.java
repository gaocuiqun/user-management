package com.sample.controller;

import com.github.apuex.springbootsolution.runtime.*;
import static com.github.apuex.springbootsolution.runtime.DateFormat.*;
import com.sample.message.*;
import com.sample.service.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import java.net.*;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping(value="user", method=RequestMethod.POST)
public class UserController {
  @Autowired
  private UserService service;

  @RequestMapping(value="create-user", consumes = "application/x-www-form-urlencoded", produces="application/json")
  public void create(@RequestParam(name = "userName") String userName,
                     @RequestParam(name="password") String password,
                     HttpServletRequest r) throws URISyntaxException {
    CreateUserCmd c = CreateUserCmd.newBuilder()
        .setUserId(userName)
        .setUserName(userName)
        .setPassword(password)
        .build();
    service.create(c, r.getUserPrincipal(), new URI(r.getRequestURI()));
  }

  @RequestMapping(value="retrieve-user-by-rowid", produces="application/json")
  public UserVo retrieveByRowid(@RequestBody RetrieveByRowidCmd c, HttpServletRequest r) throws URISyntaxException {
    return service.retrieveByRowid(c, r.getUserPrincipal(), new URI(r.getRequestURI()));
  }
  
  @RequestMapping(value="retrieve-user", produces="application/json")
  public UserVo retrieve(@RequestBody RetrieveUserCmd c, HttpServletRequest r) throws URISyntaxException {
    return service.retrieve(c, r.getUserPrincipal(), new URI(r.getRequestURI()));
  }
  
  @RequestMapping(value="update-user", produces="application/json")
  public void update(@RequestBody UpdateUserCmd c, HttpServletRequest r) throws URISyntaxException {
    service.update(c, r.getUserPrincipal(), new URI(r.getRequestURI()));
  }

  @RequestMapping(value="delete-user", produces="application/json")
  public void delete(@RequestBody DeleteUserCmd c, HttpServletRequest r) throws URISyntaxException {
    service.delete(c, r.getUserPrincipal(), new URI(r.getRequestURI()));
  }

  @RequestMapping(value="query-user", produces="application/json")
  public UserListVo query(@RequestBody QueryCommand q, HttpServletRequest r) throws URISyntaxException {
    return service.query(q, r.getUserPrincipal(), new URI(r.getRequestURI()));
  }

  @RequestMapping(value="export-user", consumes="application/json")
  public void export(@RequestBody QueryCommand q, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException {
    final UserListVo listVo = service.query(q, request.getUserPrincipal(), new URI(request.getRequestURI()));
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet("User");

    HSSFCellStyle style = wb.createCellStyle();
    style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.AQUA.getIndex());
    style.setFillPattern(FillPatternType.BIG_SPOTS);

    style = wb.createCellStyle();
    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORANGE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    short rowNumber = 0;
    exportHeaderCells(sheet.createRow(rowNumber++), style);
    for(UserVo vo: listVo.getItemsList()) {
      HSSFRow row = sheet.createRow(rowNumber++);
      exportDataCells(vo, row, style);
    }

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-disposition",String.format("attachment; filename=%sList-%s.xls", "User", formatTimestamp(new Date())));
    wb.write(response.getOutputStream());
  }

  private void exportHeaderCells(HSSFRow row, HSSFCellStyle style) {
    short colNumber = 0;
    HSSFCell cell = null;
    cell = row.createCell(colNumber++);
    cell.setCellValue("UserId");
    cell.setCellStyle(style);
    cell = row.createCell(colNumber++);
    cell.setCellValue("UserName");
    cell.setCellStyle(style);
    cell = row.createCell(colNumber++);
    cell.setCellValue("Password");
    cell.setCellStyle(style);
  }

  private void exportDataCells(UserVo vo, HSSFRow row, HSSFCellStyle style) {
    short colNumber = 0;
    HSSFCell cell = null;
    cell = row.createCell(colNumber++);
    cell.setCellValue(String.format("%s", vo.getUserId()));
    cell.setCellStyle(style);
    cell = row.createCell(colNumber++);
    cell.setCellValue(String.format("%s", vo.getUserName()));
    cell.setCellStyle(style);
    cell = row.createCell(colNumber++);
    cell.setCellValue(String.format("%s", vo.getPassword()));
    cell.setCellStyle(style);
  }
}
