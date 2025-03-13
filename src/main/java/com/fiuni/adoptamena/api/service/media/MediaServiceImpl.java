package com.fiuni.adoptamena.api.service.media;

import java.nio.file.*;
import java.util.UUID;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fiuni.adoptamena.exception_handler.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fiuni.adoptamena.api.dao.media.IMediaDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.media.MediaDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.dto.media.RequestMediaDTO;
import com.fiuni.adoptamena.api.dto.media.ResponseMediaDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.auth.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MediaServiceImpl extends BaseServiceImpl<MediaDomain, ResponseMediaDTO> implements IMediaService {

    @Autowired
    private IMediaDao mediaDao;

    @Autowired
    private IUserDao userDao;

    private final String MEDIA_FOLDER_PATH = "/etc/opt/adoptamena/media";

    @Override
    protected ResponseMediaDTO convertDomainToDto(MediaDomain domain) {
        ResponseMediaDTO mediaDto = new ResponseMediaDTO();
        mediaDto.setId(domain.getId());
        mediaDto.setUrl(domain.getUrl());
        mediaDto.setMimeType(domain.getMimeType());
        mediaDto.setUserId(domain.getUser().getId());
        mediaDto.setUploadDate(domain.getUploadDate());
        return mediaDto;
    }

    @Override
    public ResponseMediaDTO getById(Integer id) {
        MediaDomain mediaDomain = mediaDao.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Media con id " + id + " no encontrado"));
        return convertDomainToDto(mediaDomain);
    }

    @Override
    public List<ResponseMediaDTO> getAll(Pageable pageable) {
        return convertDomainListToDtoList(mediaDao.findAll(pageable).getContent());
    }

    @Override
    public void delete(Integer id) {
        //Verficar si existe el media
        mediaDao.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Media con id " + id + " no encontrado"));
        mediaDao.deleteById(id);
    }

@Override
public ResponseMediaDTO uploadMedia(RequestMediaDTO media) {
    MultipartFile file = media.getFile();

    // Verificar si el archivo es nulo o está vacío
    if (file == null || file.isEmpty()) {
        throw new BadRequestException("El archivo está vacío");
    }

    try {
        // Crear la carpeta si no existe
        Path uploadPath = Paths.get(MEDIA_FOLDER_PATH);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Obtener la extensión del archivo
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generar un nombre único para el archivo
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Guardar el archivo en la carpeta
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);

        // Construir la URL del archivo
        String fileUrl = "/media/" + uniqueFilename;

        // Obtener el tipo MIME del archivo
        String mimeType = file.getContentType();

        // Crear el objeto MediaDomain para guardar en la base de datos
        MediaDomain mediaDomain = new MediaDomain();
        mediaDomain.setMimeType(mimeType != null ? mimeType : "application/octet-stream");
        mediaDomain.setUrl(fileUrl);
        mediaDomain.setUploadDate(new Date());
        
        // Aquí debes obtener el usuario autenticado
        UserDomain user = getCurrentUser();
        mediaDomain.setUser(user);

        // Guardar en la base de datos
        mediaDomain = mediaDao.save(mediaDomain);

        // Retornar el DTO con la información guardada
        return convertDomainToDto(mediaDomain);

    } catch (Exception e) {
        throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
    }
}

        /*
     * Obtiene el usuario actualmente logueado, se implementa aqui, ya que en la
     * Interfaz base no se pasa el id del usuario como parametro, entonces no es
     * posible pasarlo por controlador se podria pasar desde el controlador
     * modificando el dto que se recibe, pero se opto por esta solucion
     */
    private UserDomain getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer userId = userDetails.getId();
            return userDao.findByIdAndIsDeletedFalse(userId)
                    .orElseThrow(() -> new BadRequestException("No se encontró el usuario logueado"));
        }
        throw new ForbiddenException("Usuario no autenticado");
    }



    //Metodo no va ser utilizado (No se crearan medias de esta forma)
    @Override
    public ResponseMediaDTO create(ResponseMediaDTO dto) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    //Metodo no va ser utilizado (No se actualizaran los medias)
    @Override
    public ResponseMediaDTO update(ResponseMediaDTO dto) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    //Metodo no va ser utilizado (No se recibiran dtos)
    @Override
    protected MediaDomain convertDtoToDomain(ResponseMediaDTO dto) {
        throw new UnsupportedOperationException("Unimplemented method 'convertDtoToDomain'");
    }
    
}
