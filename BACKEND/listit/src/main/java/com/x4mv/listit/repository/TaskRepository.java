package com.x4mv.listit.repository;

import com.x4mv.listit.model.Task;
import com.x4mv.listit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    
    List<Task> findByEncargado(User encargado);
    
    List<Task> findByEncargadoId(Integer encargadoId);
    
    List<Task> findByCompletada(Boolean completada);
    
    @Query("SELECT t FROM Task t WHERE t.encargado.id = :encargadoId AND t.completada = :completada")
    List<Task> findByEncargadoIdAndCompletada(@Param("encargadoId") Integer encargadoId, @Param("completada") Boolean completada);
    
    @Query("SELECT t FROM Task t WHERE t.nombre LIKE %:nombre%")
    List<Task> findByNombreContaining(@Param("nombre") String nombre);
}