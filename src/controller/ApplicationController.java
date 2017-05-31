package controller;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.*;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import model.DbManager;

public class ApplicationController {

    public static void main(String[] args) {
        try {
            DbManager.conectarBd();
        }
        catch(Exception ex){System.out.println("Error: unable to load driver class!"); System.exit(1);}
    }

}
