package com.x4mv.listit.service;

import com.x4mv.listit.dto.EventDTO;
import com.x4mv.listit.dto.EventResponseDTO;
import com.x4mv.listit.model.Event;
import com.x4mv.listit.model.User;
import com.x4mv.listit.model.Room;
import com.x4mv.listit.repository.EventRepository;
import com.x4mv.listit.repository.UserRepository;
import com.x4mv.listit.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    EventService(){
       configureMapping(); 
    }

    private void configureMapping(){
        // Mapear de EventDTO a Event (ignorando campos específicos)
        modelMapper.emptyTypeMap(EventDTO.class, Event.class)
            .addMappings(mapper -> {
                mapper.map(EventDTO::getTitulo, Event::setTitulo);
                mapper.map(EventDTO::getDescription, Event::setDescription);
                mapper.map(EventDTO::getFechaInicio, Event::setFechaInicio);
                mapper.map(EventDTO::getFechaFin, Event::setFechaFin);
                // Ignoramos el mapeo directo de roomId y creadorId, los manejaremos manualmente
                mapper.skip(Event::setRoom);
                mapper.skip(Event::setCreador);
                // Ignoramos el id si es una creación
                mapper.skip(Event::setId);
            });

        // Mapear de Event a EventResponseDTO
        modelMapper.emptyTypeMap(Event.class, EventResponseDTO.class)
            .addMappings(mapper -> {
                mapper.map(Event::getId, EventResponseDTO::setId);
                mapper.map(Event::getTitulo, EventResponseDTO::setTitulo);
                mapper.map(Event::getDescription, EventResponseDTO::setDescription);
                mapper.map(Event::getFechaInicio, EventResponseDTO::setFechaInicio);
                mapper.map(Event::getFechaFin, EventResponseDTO::setFechaFin);
                // Mapeo manual de las relaciones
                mapper.map(src -> src.getRoom().getId(), EventResponseDTO::setRoomId);
                mapper.map(src -> src.getRoom().getNombre(), EventResponseDTO::setRoomName);
                mapper.map(src -> src.getCreador().getId(), EventResponseDTO::setCreadorId);
                mapper.map(src -> src.getCreador().getNombre(), EventResponseDTO::setCreadorName);
            });
    }
    
    // CREATE
    public EventResponseDTO createEvent(EventDTO eventDTO) {
        
        // verificamos que exista la sala
        Room salaExistente = roomRepository.findById(eventDTO.getRoomId())
            .orElseThrow(() -> {
                throw new RuntimeException("La sala no existe");
            });
        
        // verificamos que exista el creador
        User creadorExistente = userRepository.findById(eventDTO.getCreadorId())
            .orElseThrow(() -> {
                throw new RuntimeException("El usuario creador no existe");
            });
        
        // empezamos a mapear el dto al entity
        Event nuevoEvento = modelMapper.map(eventDTO, Event.class);
        nuevoEvento.setRoom(salaExistente);
        nuevoEvento.setCreador(creadorExistente);

        // guardamos en la bd
        Event eventoGuardado = eventRepository.save(nuevoEvento);

        // mapeamos de entity a response
        return modelMapper.map(eventoGuardado, EventResponseDTO.class);
    }
    
    // READ
    public List<EventResponseDTO> getAllEvents() {
       
        // encontramos todos los eventos 
        List<Event> eventos = eventRepository.findAll();

        // mapeamos a responseDTO 
        List<EventResponseDTO> eventosResponse = new ArrayList<>();

        // recorremos los eventos para mapearlos de entity a response
        for (Event event : eventos){
            EventResponseDTO dto = modelMapper.map(event, EventResponseDTO.class);
            eventosResponse.add(dto);
        }

        return eventosResponse;
    }
    
    public EventResponseDTO getEventById(Integer id) {
        
        // verificamos que exista el evento 
        Event eventoExistente = eventRepository.findById(id)
            .orElseThrow(() -> {
                throw new RuntimeException("El evento no existe");
            });
        
        EventResponseDTO eventoResponse = modelMapper.map(eventoExistente, EventResponseDTO.class);

        return eventoResponse;
    }
    
 
    
    // UPDATE
    public EventResponseDTO updateEvent(Integer id, EventDTO eventDTO) {
        
        // verificamos que exista el evento 
        Event eventoExistente = eventRepository.findById(id)
            .orElseThrow(()->{
                throw new RuntimeException("El evento no existe");
            });

        // verificamos que exista la sala
        Room salaExistente = roomRepository.findById(eventDTO.getRoomId())
            .orElseThrow(() -> {
                throw new RuntimeException("La sala no existe");
            });
        
        // verificamos que exista el creador
        User creadorExistente = userRepository.findById(eventDTO.getCreadorId())
            .orElseThrow(() -> {
                throw new RuntimeException("El usuario creador no existe");
            });

        // mapeamos el dto a entity 
        Event eventoActualizado = modelMapper.map(eventDTO, Event.class);
        eventoActualizado.setId(id); // Mantenemos el ID del evento existente
        eventoActualizado.setRoom(salaExistente);
        eventoActualizado.setCreador(creadorExistente);

        // guardamos en la db 
        Event eventoGuardado = eventRepository.save(eventoActualizado);

        // mapeamos el entity a response 
        EventResponseDTO dto = modelMapper.map(eventoGuardado, EventResponseDTO.class); 

        return dto;
    }
    
    // DELETE
    public void deleteEvent(Integer id) {
      
        // verificamos que exista el evento 
        Event eventoExistente = eventRepository.findById(id).orElseThrow(() ->{
            throw new RuntimeException("El evento no existe");
        });

        eventRepository.delete(eventoExistente);
    }
    
    
}