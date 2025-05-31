package DATA;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base para objetos de acceso a datos (DAO)
 * Proporciona implementaciones genéricas de operaciones CRUD
 * 
 * @param <T> Tipo de entidad que maneja este DAO
 */
public abstract class BaseDAO<T> extends DataBaseHelper {
    /**
     * Nombre de la tabla en la base de datos
     */
    protected final String tableName;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 1000;
    /**
     * Constructor
     * 
     * @param tableName Nombre de la tabla en la base de datos
     */
    public BaseDAO(String tableName) {
        super();
        this.tableName = tableName;
    }
    /**
     * Ejecuta una operación SQL con reintentos y manejo de transacciones
     *
     * @param operation Operación SQL a ejecutar
     * @return true si la operación fue exitosa, false en caso contrario
     */

    protected boolean executeWithRetry(SqlOperation operation) {
        return executeWithRetry(operation, false);
    }
    protected boolean executeWithRetry(SqlOperation operation, boolean withTransaction) {
        int attempts = 0;
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                if (connection == null || connection.isClosed()) {
                    init_conexion();
                }

                if (withTransaction) {
                    connection.setAutoCommit(false);
                }

                operation.execute();

                if (withTransaction) {
                    connection.commit();
                }
                return true;

            } catch (SQLException e) {
                attempts++;
                if (withTransaction) {
                    try {
                        connection.rollback();
                    } catch (SQLException re) {
                        logSQLException("Error al realizar rollback", re);
                    }
                }

                if (attempts == MAX_RETRY_ATTEMPTS) {
                    logSQLException("Error después de " + MAX_RETRY_ATTEMPTS + " intentos", e);
                    return false;
                }

                try {
                    Thread.sleep(RETRY_DELAY_MS * attempts);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            } finally {
                if (withTransaction) {
                    try {
                        connection.setAutoCommit(true);
                    } catch (SQLException e) {
                        logSQLException("Error al restaurar autoCommit", e);
                    }
                }
                closeResources(false);
            }
        }
        return false;
    }
    @FunctionalInterface
    protected interface SqlOperation {
        void execute() throws SQLException;
    }
    /**
     * Convierte una entidad a un array de strings para mostrar
     * 
     * @param entity Entidad a convertir
     * @return Array de strings con los datos de la entidad
     */
    protected abstract String[] entityToStringArray(T entity);
    
    /**
     * Convierte un array de strings a una entidad
     * 
     * @param data Array de strings con los datos
     * @return Entidad creada a partir de los datos
     */
    protected abstract T stringArrayToEntity(String[] data);
    /**
     * Obtiene la consulta SQL para insertar una entidad
     * 
     * @return Consulta SQL de inserción
     */
    protected abstract String getInsertQuery();
    /**
     * Obtiene la consulta SQL para actualizar una entidad
     * 
     * @return Consulta SQL de actualización
     */
    protected abstract String getUpdateQuery();
    /**
     * Obtiene la consulta SQL para eliminar una entidad
     * 
     * @return Consulta SQL de eliminación
     */
    protected abstract String getDeleteQuery();
    /**
     * Obtiene la consulta SQL para buscar una entidad por ID
     * 
     * @return Consulta SQL de búsqueda por ID
     */
    protected abstract String getFindByIdQuery();
    /**
     * Obtiene la consulta SQL para listar todas las entidades
     * 
     * @return Consulta SQL para listar
     */
    protected abstract String getListAllQuery();
    /**
     * Prepara los parámetros para una consulta de inserción o actualización
     * 
     * @param entity Entidad cuyos datos se usarán para preparar los parámetros
     * @throws SQLException Si ocurre un error al preparar los parámetros
     */
    protected abstract void prepareStatementForEntity(T entity) throws SQLException;
    /**
     * Prepara los parámetros para una consulta por ID
     * 
     * @param id ID de la entidad
     * @throws SQLException Si ocurre un error al preparar los parámetros
     */
    protected abstract void prepareStatementForId(int id) throws SQLException;
    /**
     * Guarda una entidad en la base de datos
     * 
     * @param entity Entidad a guardar
     * @return Objeto con el resultado de la operación [boolean éxito, String mensaje]
     */
    public Object[] save(T entity) {
        final boolean[] success = {false};
        final String[] message = {""};

        executeWithRetry(() -> {
            ps = connection.prepareStatement(getInsertQuery());
            prepareStatementForEntity(entity);

            int rowsAffected = ps.executeUpdate();
            success[0] = rowsAffected > 0;

            if (success[0]) {
                message[0] = tableName + " - Registro insertado exitosamente.";
            } else {
                message[0] = "Error al intentar guardar los datos en " + tableName;
            }
        });

        return new Object[]{success[0], message[0]};
    }
    /**
     * Actualiza una entidad en la base de datos
     * 
     * @param entity Entidad a actualizar
     * @return Objeto con el resultado de la operación [boolean éxito, String mensaje]
     */
    public Object[] update(T entity) {
        final boolean[] success = {false};
        final String[] message = {""};

        executeWithRetry(() -> {
            ps = connection.prepareStatement(getUpdateQuery());
            prepareStatementForEntity(entity);
            int rowsAffected = ps.executeUpdate();
            success[0] = rowsAffected > 0;

            if (success[0]) {
                message[0] = tableName + " - Registro actualizado exitosamente.";
            } else {
                message[0] = "Error al intentar actualizar los datos en " + tableName;
            }
        });

        return new Object[]{success[0], message[0]};
    }
    /**
     * Elimina una entidad de la base de datos
     * 
     * @param id ID de la entidad a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public Object[] delete(int id) {
        final boolean[] success = {false};
        final String[] message = {""};

        T existingEntity = findById(id);
        if (existingEntity == null) {
            message[0] = "La entidad con ID " + id + " no existe en " + tableName;
            return new Object[]{false, message[0]};
        }

        SqlOperation operation = () -> {
            ps = connection.prepareStatement(getDeleteQuery());
            prepareStatementForId(id);

            int rowsAffected = ps.executeUpdate();
            success[0] = rowsAffected > 0;

            if (success[0]) {
                message[0] = tableName + " - Registro eliminado exitosamente.";
            } else {
                message[0] = "Error al intentar eliminar el registro en " + tableName;
            }
        };

        executeWithRetry(operation, true);
        return new Object[]{success[0], message[0]};
    }
    
    /**
     * Busca una entidad por su ID
     * 
     * @param id ID de la entidad a buscar
     * @return La entidad encontrada o null si no existe
     */
    public T findById(int id) {
        T entity = null;
        System.out.println("Buscando entidad por ID: " + id + " en " + tableName);

        int attempts = 0;
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                if (connection == null || connection.isClosed()) {
                    init_conexion();
                }

                ps = connection.prepareStatement(getFindByIdQuery());
                prepareStatementForId(id);

                resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    String[] data = arrayData(resultSet);
                    entity = stringArrayToEntity(data);
                    System.out.println("Entidad encontrada: " + entity);
                    return entity;
                }
                break; // Si llegamos aquí, la consulta fue exitosa aunque no encontró resultados

            } catch (SQLException e) {
                attempts++;
                if (attempts == MAX_RETRY_ATTEMPTS) {
                    logSQLException("Error después de " + MAX_RETRY_ATTEMPTS + " intentos al buscar entidad por ID en " + tableName, e);
                    break;
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS * attempts); // Retraso exponencial
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } finally {
                closeResources(false);
            }
        }

        return entity;
    }
    
    /**
     * Lista todas las entidades de la tabla
     * 
     * @return Lista de entidades
     */
    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
        SqlOperation operation = () -> {
            ps = connection.prepareStatement(getListAllQuery());
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String[] data = arrayData(resultSet);
                T entity = stringArrayToEntity(data);
                entities.add(entity);
            }
        };
        executeWithRetry(operation);
        return entities;
    }
    /**
     * Convierte un LocalDateTime a Timestamp para usar en consultas SQL
     * 
     * @param dateTime Fecha y hora a convertir
     * @return Timestamp equivalente
     */
    protected Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? Timestamp.valueOf(dateTime) : null;
    }
    
    /**
     * Convierte un String a LocalDateTime
     * 
     * @param dateTimeStr String con la fecha y hora
     * @return LocalDateTime equivalente o null si el string es nulo o vacío
     */
    protected LocalDateTime toLocalDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return Timestamp.valueOf(dateTimeStr).toLocalDateTime();
        } catch (IllegalArgumentException e) {
            System.err.println("Error al convertir fecha: " + dateTimeStr);
            return null;
        }
    }
}