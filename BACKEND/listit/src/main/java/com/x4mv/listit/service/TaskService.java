package com.x4mv.listit.service;

import com.x4mv.listit.dto.TaskDTO;
import com.x4mv.listit.dto.TaskResponseDTO;
import com.x4mv.listit.model.Task;
import com.x4mv.listit.model.User;
import com.x4mv.listit.repository.TaskRepository;
import com.x4mv.listit.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @PostConstruct
    private void configureMapping() {
        // Mapear de TaskDTO a Task (ignorando campos específicos)
        modelMapper.emptyTypeMap(TaskDTO.class, Task.class)
                .addMappings(mapper -> {
                    mapper.map(TaskDTO::getNombre, Task::setNombre);
                    mapper.map(TaskDTO::getDescription, Task::setDescription);
                    mapper.map(TaskDTO::getPrecio, Task::setPrecio);
                    mapper.map(TaskDTO::getCantidad, Task::setCantidad);
                    mapper.map(TaskDTO::getComentario, Task::setComentario);
                    mapper.map(TaskDTO::getCompletada, Task::setCompletada);
                    // Ignoramos el mapeo directo de encargadoId, lo manejaremos manualmente
                    mapper.skip(Task::setEncargado);
                    // Ignoramos el id si es una creación
                    mapper.skip(Task::setId);
                });

        // Mapear de Task a TaskResponseDTO
        modelMapper.emptyTypeMap(Task.class, TaskResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Task::getId, TaskResponseDTO::setId);
                    mapper.map(Task::getNombre, TaskResponseDTO::setNombre);
                    mapper.map(Task::getDescription, TaskResponseDTO::setDescription);
                    mapper.map(Task::getPrecio, TaskResponseDTO::setPrecio);
                    mapper.map(Task::getCantidad, TaskResponseDTO::setCantidad);
                    mapper.map(Task::getComentario, TaskResponseDTO::setComentario);
                    mapper.map(Task::getCompletada, TaskResponseDTO::setCompletada);
                    // Mapeo manual del encargado
                    mapper.map(src -> src.getEncargado().getId(), TaskResponseDTO::setEncargadoId);
                    mapper.map(src -> src.getEncargado().getNombre(), TaskResponseDTO::setEncargadoName);
                });
    }

    // CREATE
    public TaskResponseDTO createTask(TaskDTO taskDTO) {

        // verificamos que el usuario encargado exista
        User encargadoExistente = userRepository.findById(taskDTO.getEncargadoId())
                .orElseThrow(() -> {
                    throw new RuntimeException("El usuario encargado no existe");
                });

        // empezamos a mapear el dto al entity
        Task nuevaTarea = modelMapper.map(taskDTO, Task.class);
        nuevaTarea.setEncargado(encargadoExistente);

        // guardamos en la bd
        Task tareaGuardada = taskRepository.save(nuevaTarea);

        // mapeamos de entity a response
        return modelMapper.map(tareaGuardada, TaskResponseDTO.class);
    }

    // READ
    public List<TaskResponseDTO> getAllTasks() {

        // encontramos todas las tareas
        List<Task> tareas = taskRepository.findAll();

        // mapeamos a responseDTO
        List<TaskResponseDTO> tareasResponse = new ArrayList<>();

        // recorremos las tareas para mapearlas de entity a response
        for (Task task : tareas) {
            TaskResponseDTO dto = modelMapper.map(task, TaskResponseDTO.class);
            tareasResponse.add(dto);
        }

        return tareasResponse;
    }

    public TaskResponseDTO getTaskById(Integer id) {

        // verificamos que exista la tarea
        Task tareaExistente = taskRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RuntimeException("La tarea no existe");
                });

        TaskResponseDTO tareaResponse = modelMapper.map(tareaExistente, TaskResponseDTO.class);

        return tareaResponse;
    }

    public List<TaskResponseDTO> getTasksByUserId(Integer userId) {

        // verificamos que el usuario exista
        userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new RuntimeException("El usuario no existe");
                });

        // encontramos las tareas del usuario
        List<Task> tareas = taskRepository.findByEncargadoId(userId);

        // mapeamos a responseDTO
        List<TaskResponseDTO> tareasResponse = new ArrayList<>();

        // recorremos las tareas para mapearlas de entity a response
        for (Task task : tareas) {
            TaskResponseDTO dto = modelMapper.map(task, TaskResponseDTO.class);
            tareasResponse.add(dto);
        }

        return tareasResponse;
    }

    // UPDATE
    public TaskResponseDTO updateTask(Integer id, TaskDTO taskDTO) {

        // verificamos que exista la tarea
        taskRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RuntimeException("La tarea no existe");
                });

        // verificamos que el usuario encargado exista
        User encargadoExistente = userRepository.findById(taskDTO.getEncargadoId())
                .orElseThrow(() -> {
                    throw new RuntimeException("El usuario encargado no existe");
                });

        // mapeamos el dto a entity
        Task tareaActualizada = modelMapper.map(taskDTO, Task.class);
        tareaActualizada.setId(id); // Mantenemos el ID de la tarea existente
        tareaActualizada.setEncargado(encargadoExistente);

        // guardamos en la db
        Task tareaGuardada = taskRepository.save(tareaActualizada);

        // mapeamos el entity a response
        TaskResponseDTO dto = modelMapper.map(tareaGuardada, TaskResponseDTO.class);

        return dto;
    }

    // DELETE
    public void deleteTask(Integer id) {

        // verificamos que exista la tarea
        Task tareaExistente = taskRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("La tarea no existe");
        });

        taskRepository.delete(tareaExistente);
    }
}
