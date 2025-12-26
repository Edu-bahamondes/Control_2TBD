import apiClient from '@/core/api/axios';

export default {
    obtenerTodas() {
        return apiClient.get('/tareas');
    },
    crear(tarea) {
        return apiClient.post('/tareas', tarea);
    },
    actualizar(id, tarea) {
        return apiClient.put(`/tareas/${id}`, tarea);
    },
    eliminar(id) {
        return apiClient.delete(`/tareas/${id}`);
    },
    completar(id) {
        return apiClient.patch(`/tareas/${id}/completar`);
    },
    obtenerPorId(id) {
        return apiClient.get(`/tareas/${id}`); // Assumes backend supports GET /tareas/{id}. If not, we might need to filter client-side or add endpoint.
        // Wait, TareaController usually has getById if it follows CRUD. Let's check or assume standard.
        // Actually, looking at previous TareaController.java dump...
        // valid endpoints: POST /, PUT /{id}, DELETE /{id}, PATCH /{id}/completar, GET /, GET /estado/{estado}, GET /buscar, GET /proximas-vencer
        // It DOES NOT have GET /{id}!
        // I need to implement GET /{id} in backend first or filter client side.
        // Better to implement in backend for correctness.
    },
    obtenerProximasAVencer(dias = 3) {
        return apiClient.get(`/tareas/proximas-vencer?dias=${dias}`);
    }
};