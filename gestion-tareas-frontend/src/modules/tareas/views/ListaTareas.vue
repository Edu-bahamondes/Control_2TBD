<template>
  <div class="container">
    <div class="header">
      <button class="btn-nueva" @click="$router.push('/tareas/nueva')">➕ Nueva Tarea</button>
    </div>

    <!-- Tareas Próximas a Vencer -->
    <div v-if="tareasProximas.length > 0" class="card warning-card">
      <div class="warning-header">
        <h3>⚠️ ¡Atención! Tareas Próximas a Vencer</h3>
      </div>
      <div class="warning-list">
        <div v-for="tarea in tareasProximas" :key="'prox-' + tarea.idTarea" class="warning-item">
          <span class="warning-title">{{ tarea.titulo }}</span>
          <span class="warning-date">{{ tarea.fechaVencimiento }}</span>
          <button class="btn-small-action" @click="$router.push(`/tareas/editar/${tarea.idTarea}`)">✏️</button>
        </div>
      </div>
    </div>

    <div v-if="loading">Cargando tareas...</div>

    <div v-else class="grid">
      <div v-for="tarea in tareas" :key="tarea.idTarea" class="card task-card">
        <div class="task-header">
          <h3>{{ tarea.titulo }}</h3>
          <span :class="['badge', tarea.estado === 'COMPLETADA' ? 'done' : 'pending']">
            {{ tarea.estado }}
          </span>
        </div>
        <p>{{ tarea.descripcion }}</p>
        <p><small>🗓️ Vence: {{ tarea.fechaVencimiento }}</small></p>
        <p><small>🏢 Sector: {{ tarea.nombreSector }}</small></p>

        <div class="actions">
          <button v-if="tarea.estado !== 'COMPLETADA'" class="btn-completar" @click="completar(tarea.idTarea)">
            ✅ Completar
          </button>
          <button class="btn-editar" @click="$router.push(`/tareas/editar/${tarea.idTarea}`)">✏️ Editar</button>
          <button class="btn-eliminar" @click="eliminar(tarea.idTarea)">🗑️ Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import tareasService from '../api/tareas.service';

const tareas = ref([]);
const tareasProximas = ref([]);
const loading = ref(true);

const cargarTareas = async () => {
  loading.value = true;
  try {
    const [respTodas, respProximas] = await Promise.all([
      tareasService.obtenerTodas(),
      tareasService.obtenerProximasAVencer(3) // 3 días
    ]);
    tareas.value = respTodas.data;
    tareasProximas.value = respProximas.data;
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const completar = async (id) => {
  if(!confirm("¿Marcar como completada?")) return;
  try {
    await tareasService.completar(id);
    await cargarTareas(); // Recargar para ver el cambio
    alert('✅ Tarea marcada como completada');
  } catch (e) {
    console.error('Error al completar tarea:', e);
    const mensajeError = e.response?.data?.message || e.response?.data || e.message || 'Error desconocido';
    alert(`Error al completar la tarea: ${mensajeError}`);
  }
};

const eliminar = async (id) => {
  if(!confirm("¿Eliminar tarea?")) return;
  try {
    await tareasService.eliminar(id);
    await cargarTareas(); // Recargar para ver el cambio
  } catch (e) {
    console.error('Error al eliminar tarea:', e);
    alert('Error al eliminar la tarea. Por favor, intenta de nuevo.');
  }
};

onMounted(() => {
  cargarTareas();
});
</script>

<style scoped>
.container { padding: 20px; max-width: 1000px; margin: 0 auto; }
.header { display: flex; justify-content: flex-end; align-items: center; margin-bottom: 20px; gap: 10px;}
.grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
.task-header { display: flex; justify-content: space-between; align-items: start; }
.badge { padding: 4px 8px; border-radius: 12px; font-size: 0.8em; color: black; font-weight: bold;}
.badge.done { background-color: #42b883; }
.badge.pending { background-color: #f1c40f; }
.actions { margin-top: 15px; display: flex; gap: 10px; flex-wrap: wrap; }

/* Botón Nueva Tarea */
.btn-nueva {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.btn-nueva:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

/* Botón Completar */
.btn-completar {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: all 0.3s ease;
}

.btn-completar:hover { opacity: 0.9; transform: translateY(-1px); }

/* Botón Editar */
.btn-editar {
  background: linear-gradient(135deg, #f09819 0%, #edde5d 100%);
  color: #333;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: all 0.3s ease;
}

.btn-editar:hover { opacity: 0.9; transform: translateY(-1px); }

/* Botón Eliminar */
.btn-eliminar {
  background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: all 0.3s ease;
}

.btn-eliminar:hover { opacity: 0.9; transform: translateY(-1px); }

/* Warning Card */
.warning-card {
  background: linear-gradient(135deg, #fff8e1 0%, #ffecb3 100%); /* Softer yellow gradient */
  border-left: 5px solid #ffc107; /* Strong accent border */
  color: #5d4037; /* Dark brown for better contrast than yellow-on-yellow */
  margin-bottom: 25px;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0,0,0,0.05);
}

.warning-header h3 { 
  margin: 0 0 15px 0; 
  font-size: 1.2rem;
  color: #d35400; /* Darker orange text for header */
  display: flex;
  align-items: center;
  gap: 10px;
}

.warning-list { display: flex; flex-direction: column; gap: 10px; }

.warning-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white; /* White background for items for max contrast */
  padding: 12px 16px;
  border-radius: 8px;
  border: 1px solid rgba(0,0,0,0.05);
  transition: transform 0.2s;
}

.warning-item:hover {
  transform: translateX(5px);
  border-color: #ffc107;
}

.warning-title { 
  font-weight: 700; 
  color: #2c3e50;
  font-size: 1rem;
}

.warning-date {
  color: #e67e22;
  font-weight: 500;
  font-size: 0.9rem;
  background: rgba(230, 126, 34, 0.1);
  padding: 4px 8px;
  border-radius: 4px;
}

.btn-small-action { 
  border: none; 
  background: #fdf2e9; 
  cursor: pointer; 
  font-size: 1.1rem; 
  padding: 6px 10px;
  border-radius: 6px;
  transition: all 0.2s;
  color: #d35400;
}

.btn-small-action:hover {
  background: #d35400;
  color: white;
}

@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(255, 193, 7, 0.4); }
  70% { box-shadow: 0 0 0 10px rgba(255, 193, 7, 0); }
  100% { box-shadow: 0 0 0 0 rgba(255, 193, 7, 0); }
}
</style>