package pe.edu.cibertec.fronted_integrantes.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.fronted_integrantes.client.AutenticacionClient;
import pe.edu.cibertec.fronted_integrantes.dto.IntegrantesResponseDTO;
import pe.edu.cibertec.fronted_integrantes.dto.LoginRequestDTO;
import pe.edu.cibertec.fronted_integrantes.dto.LoginResponseDTO;
import pe.edu.cibertec.fronted_integrantes.viewmodel.LoginModel;


import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    AutenticacionClient autenticacionClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("codAlumno") String codAlumno,
                             @RequestParam("password") String password,
                             Model model) {
        try {
            if (codAlumno == null || codAlumno.trim().length() == 0 ||
                    password == null || password.trim().length() == 0) {

                LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";
            }

            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(codAlumno, password);
            ResponseEntity<LoginResponseDTO> responseEntity = autenticacionClient.login(loginRequestDTO);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                LoginResponseDTO loginResponseDTO = responseEntity.getBody();

                if (loginResponseDTO.codigo().equals("00")) {

                    LoginModel loginModel = new LoginModel("00", "inicio correcto",loginResponseDTO.nombres()+" "+loginResponseDTO.apellidos());

                    ResponseEntity<List<IntegrantesResponseDTO>> responseEntity2 = restTemplate.exchange(
                            "/get-integrantes", HttpMethod.GET, null, new ParameterizedTypeReference<List<IntegrantesResponseDTO>>() {});

                    if (responseEntity2.getStatusCode().is2xxSuccessful()) {
                        List<IntegrantesResponseDTO> integrantes = responseEntity2.getBody();
                        model.addAttribute("listaIntegrantes", integrantes);
                    }


                    return "principal";
                } else {
                    LoginModel loginModel = new LoginModel("02", "Error: Credenciales incorrectas", "");
                    model.addAttribute("loginModel", loginModel);
                    return "inicio";
                }

            } else {
                LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema http", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";
            }

        } catch (Exception e) {
            LoginModel loginModel = new LoginModel("99", "Error en el proceso de autenticación", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";
        }
    }



}
