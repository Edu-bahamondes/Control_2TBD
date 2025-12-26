package cl.usach.tbd.gestiontareasbackend.scheduler;

import cl.usach.tbd.gestiontareasbackend.dto.TareaResponse;
import cl.usach.tbd.gestiontareasbackend.entity.Usuario;
import cl.usach.tbd.gestiontareasbackend.repository.UsuarioRepository;
import cl.usach.tbd.gestiontareasbackend.service.TareaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final TareaService tareaService;
    private final UsuarioRepository usuarioRepository;

    // Se ejecuta todos los días a las 9:00 AM
    // Para pruebas rápidas se puede usar: @Scheduled(fixedRate = 60000) // Cada
    // minuto
    @Scheduled(cron = "0 0 9 * * *")
    public void verificarTareasProximasAVencer() {
        log.info("Iniciando verificación de tareas próximas a vencer...");

        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario usuario : usuarios) {
            try {
                // Buscar tareas que vencen en los próximos 3 días
                List<TareaResponse> tareasProximas = tareaService.obtenerTareasProximasAVencer(usuario.getIdUsuario(),
                        3);

                if (!tareasProximas.isEmpty()) {
                    log.info("Usuario: {} tiene {} tareas próximas a vencer.",
                            usuario.getNombreUsuario(), tareasProximas.size());

                    // Aquí se integraría con un servicio de Email o Push Notification real
                    for (TareaResponse tarea : tareasProximas) {
                        log.info("  -> Tarea próxima a vencer: '{}' (Fecha: {})",
                                tarea.getTitulo(), tarea.getFechaVencimiento());
                    }
                }
            } catch (Exception e) {
                log.error("Error verificando tareas para usuario {}: {}", usuario.getNombreUsuario(), e.getMessage());
            }
        }

        log.info("Verificación de tareas completada.");
    }
}
