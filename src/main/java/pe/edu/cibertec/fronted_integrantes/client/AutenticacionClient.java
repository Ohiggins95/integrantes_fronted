package pe.edu.cibertec.fronted_integrantes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.fronted_integrantes.config.AutenticacionFeingConfig;
import pe.edu.cibertec.fronted_integrantes.dto.IntegrantesResponseDTO;
import pe.edu.cibertec.fronted_integrantes.dto.LoginRequestDTO;
import pe.edu.cibertec.fronted_integrantes.dto.LoginResponseDTO;

import java.util.List;

@FeignClient(name = "autenticacion", url = "https://backend-integrantes.azurewebsites.net", configuration = AutenticacionFeingConfig.class)
public interface AutenticacionClient {



    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO);

    @GetMapping("/get-integrantes")
    ResponseEntity<List<IntegrantesResponseDTO>> listarIntegrantes();



}
