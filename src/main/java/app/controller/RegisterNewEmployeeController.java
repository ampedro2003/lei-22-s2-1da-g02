package app.controller;


import app.domain.model.Company;
import app.domain.model.Employee;
import app.domain.shared.Constants;
import app.ui.console.RegisterNewEmployeeDto;

import java.util.ArrayList;


/**
 * US010 - Register New Employee Controller
 *
 * @author Guilherme Sousa <1211073@isep.ipp.pt>
 */

public class RegisterNewEmployeeController {

    private Company company = App.getInstance().getCompany();

    public RegisterNewEmployeeController() {
    }

    public ArrayList<String> getRolesList() {
        return company.getRolesList();
    }
    public boolean registerNewEmployee(RegisterNewEmployeeDto dto) {
        if (company.registerNewEmployee(dto)) {
            return true;
        }
        return false;
    }

    public void saveCreatedEmployee(RegisterNewEmployeeDto dto) {
        company.saveCreatedEmployee(dto);
    }
}