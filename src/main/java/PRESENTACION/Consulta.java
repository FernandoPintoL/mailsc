package PRESENTACION;

import NEGOCIO.*;
import UTILS.ConstGlobal;
import UTILS.Help;
import UTILS.Subject;
import CONNECTION.Pop3;
import CONNECTION.Smtp;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import OBJECT.Mensaje;

import java.time.format.DateTimeFormatter;

public class Consulta {
    private final NCliente NEGOCIO_CLIENTE;
    private final NContrato NEGOCIO_CONTRATO;
    private final NEmpleado NEGOCIO_EMPLEADO;
    private final NEmpleadoEquipoTrabajos NEGOCIO_EMPLEADO_EQUIPO_TRABAJO;
    private final NEquipoTrabajos NEGOCIO_EQUIPO_TRABAJO;
    private final NEquipoTrabajoServicio NEGOCIO_EQUIPO_TRABAJO_SERVICIO;
    private final NIncidencia NEGOCIO_INCIDENCIA;
    private final NContratoIncidencia NEGOCIO_CONTRATO_INCIDENCIA;
    private final NProducto NEGOCIO_PRODUCTO;
    private final NProductoServicio NEGOCIO_PRODUCTO_SERVICIO;
    private final NServicio NEGOCIO_SERVICIO;
    private final NReporte NEGOCIO_REPORTE;
    private Pop3 pop3;
    public Consulta() throws IOException {
        // negocio o bussinness
        NEGOCIO_CLIENTE = new NCliente();
        NEGOCIO_CONTRATO= new NContrato();
        NEGOCIO_EMPLEADO = new NEmpleado();
        NEGOCIO_EMPLEADO_EQUIPO_TRABAJO = new NEmpleadoEquipoTrabajos();
        NEGOCIO_EQUIPO_TRABAJO = new NEquipoTrabajos();
        NEGOCIO_EQUIPO_TRABAJO_SERVICIO = new NEquipoTrabajoServicio();
        NEGOCIO_INCIDENCIA = new NIncidencia();
        NEGOCIO_CONTRATO_INCIDENCIA = new NContratoIncidencia();
        NEGOCIO_PRODUCTO = new NProducto();
        NEGOCIO_PRODUCTO_SERVICIO = new NProductoServicio();
        NEGOCIO_SERVICIO = new NServicio();
        NEGOCIO_REPORTE = new NReporte();
    }
    public int getCantidadMails() throws IOException {
        pop3 = new Pop3(ConstGlobal.SERVIDOR, ConstGlobal.PORT_POP3);
        pop3.login(ConstGlobal.USER, ConstGlobal.PASS);
        pop3.list();
        String number = pop3.number;
        //System.out.println("number:::: "+number);
        pop3.retr(Integer.parseInt(number));
        pop3.quit();
        pop3.close();
        return Integer.parseInt(number);
    }
    public void newMensaje() throws IOException {
        String token = pop3.Token();
        System.out.println("TOKEN: " + token);
        String email = pop3.getEmail();
        if (token.toUpperCase().contains(Help.HELP)) {
            sendMail(email, "AYUDA", Help.ContenidoHelp());
            return;
        }
        Mensaje msj = Subject.subject(token, email);
        if (msj == null) {
            sendMail(email, "Error de comandos", comandoIncorrecto(token));
            return;
        }
        try {
            negocioAction(msj);
        } catch (NumberFormatException | ParseException e) {
            sendMail(msj.getEmisor(), "Error al convertir un parametros".toUpperCase(), errorConvertirParametros(msj));
        } catch (SQLException | IOException ex) {
            sendMail(msj.getEmisor(), "Error de conexion".toUpperCase(), errorConexion(ex.toString(), msj));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void negocioAction(Mensaje msj) throws Exception {
        String PARAMETROS_INCORRECTOS = "PARAMETROS INCORRECTOS | " + msj.getParametros();
        String tableAction = msj.tableAction();
        LocalTime horaActual = LocalTime.now();
        DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HH-mm-ss");
        String horaFormateada = horaActual.format(formatter_time);
        switch (tableAction) {
            //START CLIENTE
            case Help.CLIENTE + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM5) {
                    String ci = msj.getParametros().get(0);
                    String nombre = msj.getParametros().get(1);
                    String telefono = msj.getParametros().get(2);
                    String direccion = msj.getParametros().get(3);
                    String tipo_cliente = msj.getParametros().get(4);
                    if (nombre == null || nombre.trim().isEmpty() || nombre.length() <= 1) {
                        sendMail(msj.getEmisor(), msj.evento(), " El nombre es requerido. || Mayor a 3 caracteres.".toUpperCase());
                        break;
                    }
                    if (ci.length() <= 1) {
                        sendMail(msj.getEmisor(), msj.evento(), " El CI es requerido y debe ser mayor a 0. || Mayor a 3 caracteres".toUpperCase());
                        break;
                    }
                    Object[] responsse = NEGOCIO_CLIENTE.guardar(ci, nombre, direccion, telefono, tipo_cliente);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CLIENTE + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM6) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String ci = msj.getParametros().get(1).trim();
                    String nombre = msj.getParametros().get(2).trim();
                    String telefono = msj.getParametros().get(3).trim();
                    String direccion = msj.getParametros().get(4).trim();
                    String tipo_cliente = msj.getParametros().get(5).trim();
                    if (nombre.trim().isEmpty() || nombre.length() <= 2) {
                        sendMail(msj.getEmisor(), msj.evento(), "Los datos para la tabla ".toUpperCase() + msj.getTable() + ": El nombre es requerido. || Mayor a 3 caracteres.");
                        break;
                    }
                    if (ci.length() <= 1) {
                        sendMail(msj.getEmisor(), msj.evento(), "Los datos para la tabla ".toUpperCase() + msj.getTable() + ": El CI es requerido y debe ser mayor a 0. || Mayor a 3 caracteres");
                        break;
                    }
                    Object[] responsse = NEGOCIO_CLIENTE.modificar(id,ci,nombre,telefono,direccion,tipo_cliente);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CLIENTE + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_CLIENTE.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CLIENTE + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_CLIENTE.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.clienteHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.CLIENTE + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_CLIENTE.listar();
                list(Help.clienteHeader, lista, msj);
                break;
            }
            //END CLIENTE
            //START EMPLEADO
            case Help.EMPLEADO + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM5) {
                    String ci = msj.getParametros().get(0);
                    String nombre = msj.getParametros().get(1);
                    String telefono = msj.getParametros().get(2);
                    String puesto = msj.getParametros().get(3);
                    String estado = msj.getParametros().get(4);
                    if (nombre == null || nombre.trim().isEmpty() || nombre.length() <= 1) {
                        sendMail(msj.getEmisor(), msj.evento(), " El nombre es requerido. || Mayor a 3 caracteres.".toUpperCase());
                        break;
                    }
                    if (ci.length() <= 1) {
                        sendMail(msj.getEmisor(), msj.evento(), " El CI es requerido y debe ser mayor a 0. || Mayor a 3 caracteres".toUpperCase());
                        break;
                    }
                    Object[] responsse = NEGOCIO_EMPLEADO.guardar(ci, nombre, telefono, puesto, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EMPLEADO + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM6) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String ci = msj.getParametros().get(1);
                    String nombre = msj.getParametros().get(2);
                    String telefono = msj.getParametros().get(3);
                    String puesto = msj.getParametros().get(4);
                    String estado = msj.getParametros().get(5);
                    if (nombre == null || nombre.trim().isEmpty() || nombre.length() <= 2) {
                        sendMail(msj.getEmisor(), msj.evento(), "Los datos para la tabla ".toUpperCase() + msj.getTable() + ": El nombre es requerido. || Mayor a 3 caracteres.");
                        break;
                    }
                    if (ci.length() <= 1) {
                        sendMail(msj.getEmisor(), msj.evento(), "Los datos para la tabla ".toUpperCase() + msj.getTable() + ": El CI es requerido y debe ser mayor a 0. || Mayor a 3 caracteres");
                        break;
                    }
                    Object[] responsse = NEGOCIO_EMPLEADO.modificar(id, ci, nombre, telefono, puesto, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EMPLEADO + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_EMPLEADO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EMPLEADO + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_EMPLEADO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.empleadoHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.EMPLEADO + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_EMPLEADO.listar();
                list(Help.empleadoHeader, lista, msj);
                break;
            }
            //END EMPLEADO
            //START PRODUCTO
            case Help.PRODUCTO + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM4) {
                    String nombre = msj.getParametros().get(0);
                    String descripcion = msj.getParametros().get(1);
                    double precio = Double.parseDouble(msj.getParametros().get(2).trim());
                    double stock = Double.parseDouble(msj.getParametros().get(3).trim());
                    if (nombre == null || nombre.trim().isEmpty() || nombre.length() <= 1) {
                        sendMail(msj.getEmisor(), msj.evento(), " El nombre es requerido. || Mayor a 3 caracteres.".toUpperCase());
                        break;
                    }
                    Object[] responsse = NEGOCIO_PRODUCTO.guardar(nombre, descripcion, precio, stock);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.PRODUCTO + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM5) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String nombre = msj.getParametros().get(1);
                    String descripcion = msj.getParametros().get(2);
                    double precio = Double.parseDouble(msj.getParametros().get(3).trim());
                    double stock = Double.parseDouble(msj.getParametros().get(4).trim());
                    if (nombre == null || nombre.trim().isEmpty() || nombre.length() <= 2) {
                        sendMail(msj.getEmisor(), msj.evento(), "Los datos para la tabla ".toUpperCase() + msj.getTable() + ": El nombre es requerido. || Mayor a 3 caracteres.");
                        break;
                    }
                    Object[] responsse = NEGOCIO_PRODUCTO.modificar(id, nombre, descripcion, precio, stock);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.PRODUCTO + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_PRODUCTO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.PRODUCTO + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_PRODUCTO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.productoHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.PRODUCTO + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_PRODUCTO.listar();
                list(Help.productoHeader, lista, msj);
                break;
            }
            //END PRODUCTO
            //START PRODUCTO SERVICIOS
            case Help.PRODUCTO_SERVICIOS + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM3) {
                    int producto_id = Integer.parseInt(msj.getParametros().get(0).trim());
                    int servicio_id = Integer.parseInt(msj.getParametros().get(1).trim());
                    double cantidad = Double.parseDouble(msj.getParametros().get(2).trim());
                    Object[] responsse = NEGOCIO_PRODUCTO_SERVICIO.guardar(producto_id, servicio_id, cantidad);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.PRODUCTO_SERVICIOS + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM2) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    double cantidad = Double.parseDouble(msj.getParametros().get(1).trim());
                    Object[] responsse = NEGOCIO_PRODUCTO_SERVICIO.modificar(id, cantidad);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.PRODUCTO_SERVICIOS + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_PRODUCTO_SERVICIO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.PRODUCTO_SERVICIOS + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_PRODUCTO_SERVICIO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.productoServicioHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.PRODUCTO_SERVICIOS + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_PRODUCTO_SERVICIO.listar();
                list(Help.productoServicioHeader, lista, msj);
                break;
            }
            //END PRODCUTO SERVICIO
            //START SERVICIOS
            case Help.SERVICIO + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM5) {
                    String nombre = msj.getParametros().get(0);
                    String descripcion = msj.getParametros().get(1);
                    double precio = Double.parseDouble(msj.getParametros().get(2).trim());
                    String frecuencia = msj.getParametros().get(3).trim();
                    String estado = msj.getParametros().get(4);
                    Object[] responsse = NEGOCIO_SERVICIO.guardar(nombre, descripcion, precio, frecuencia, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.SERVICIO + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM6) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String nombre = msj.getParametros().get(1);
                    String descripcion = msj.getParametros().get(2);
                    double precio = Double.parseDouble(msj.getParametros().get(3).trim());
                    String frecuencia = msj.getParametros().get(4).trim();
                    String estado = msj.getParametros().get(5);
                    Object[] responsse = NEGOCIO_SERVICIO.modificar(id, nombre, descripcion, precio, frecuencia, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.SERVICIO + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_SERVICIO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.SERVICIO + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_SERVICIO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.servicioHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.SERVICIO + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_SERVICIO.listar();
                list(Help.servicioHeader, lista, msj);
                break;
            }
            //END SERVICIO
            //START EQUIPO TRABAJO SERVICIOS
            case Help.EQUIPO_TRABAJO_SERVICIO + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM3) {
                    int equipo_trabajo_id = Integer.parseInt(msj.getParametros().get(0).trim());
                    int servicio_id = Integer.parseInt(msj.getParametros().get(1).trim());
                    String estado = msj.getParametros().get(2).trim();
                    Object[] responsse = NEGOCIO_EQUIPO_TRABAJO_SERVICIO.guardar(equipo_trabajo_id, servicio_id, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EQUIPO_TRABAJO_SERVICIO + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM2) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String estado = msj.getParametros().get(1);
                    Object[] responsse = NEGOCIO_EQUIPO_TRABAJO_SERVICIO.modificar(id, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EQUIPO_TRABAJO_SERVICIO + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_EQUIPO_TRABAJO_SERVICIO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EQUIPO_TRABAJO_SERVICIO + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_EQUIPO_TRABAJO_SERVICIO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.equipoTrabajoServicioHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.EQUIPO_TRABAJO_SERVICIO + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_EQUIPO_TRABAJO_SERVICIO.listar();
                list(Help.equipoTrabajoServicioHeader, lista, msj);
                break;
            }
            //END EQUIPO TRABAJO SERVICIO
            //START CONTRATOS
            case Help.CONTRATOS + "_" + Help.ADD: {
                System.out.println("TAMANHO PARAMETRO: "+msj.getParametros().size());
                if (msj.getParametros().size() == Help.LENPARAM7) {
                    String descripcion = msj.getParametros().get(0);
                    double precio_total = Double.parseDouble(msj.getParametros().get(1).trim());
                    String estado = msj.getParametros().get(2);
                    String f_ini = msj.getParametros().get(3).trim();
                    String f_fin = msj.getParametros().get(4).trim();
                    int cliente_id = Integer.parseInt(msj.getParametros().get(5).trim());
                    int servicio_id = Integer.parseInt(msj.getParametros().get(6).trim());
                    // Formateador para el formato yyyy-MM-dd
                    DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    // Parsear String a LocalDate
                    LocalDate localDate_ini = LocalDate.parse(f_ini, date_formatter);
                    LocalDate localDate_fin = LocalDate.parse(f_fin, date_formatter);
                    // Convertir LocalDate a LocalDateTime (agregando hora 00:00)
                    LocalDateTime localDateTime_ini = localDate_ini.atTime(0, 0); // Hora 00:00
                    LocalDateTime localDateTime_fin = localDate_fin.atTime(0, 0); // Hora 00:00
                    // Convertir LocalDateTime a Timestamp
                    Timestamp fecha_inicio = Timestamp.valueOf(localDateTime_ini);
                    Timestamp fecha_fin = Timestamp.valueOf(localDateTime_fin);
                    Object[] responsse = NEGOCIO_CONTRATO.guardar(cliente_id, servicio_id, descripcion, precio_total, estado, fecha_inicio, fecha_fin);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CONTRATOS + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM6) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String descripcion = msj.getParametros().get(1);
                    double precio_total = Double.parseDouble(msj.getParametros().get(2).trim());
                    String estado = msj.getParametros().get(3).trim();
                    String f_ini = msj.getParametros().get(4);
                    String f_fin = msj.getParametros().get(5);

                    // Formateador para el formato yyyy-MM-dd
                    DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    // Parsear String a LocalDate
                    LocalDate localDate_ini = LocalDate.parse(f_ini, date_formatter);
                    LocalDate localDate_fin = LocalDate.parse(f_fin, date_formatter);

                    // Convertir LocalDate a LocalDateTime (agregando hora 00:00)
                    LocalDateTime localDateTime_ini = localDate_ini.atTime(0, 0); // Hora 00:00
                    LocalDateTime localDateTime_fin = localDate_fin.atTime(0, 0); // Hora 00:00

                    // Convertir LocalDateTime a Timestamp
                    Timestamp fecha_inicio = Timestamp.valueOf(localDateTime_ini);
                    Timestamp fecha_fin = Timestamp.valueOf(localDateTime_fin);
                    Object[] responsse = NEGOCIO_CONTRATO.modificar(id, descripcion, precio_total, estado, fecha_inicio, fecha_fin);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CONTRATOS + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_CONTRATO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CONTRATOS + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_CONTRATO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.contratoHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.CONTRATOS + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_CONTRATO.listar();
                list(Help.contratoHeader, lista, msj);
                break;
            }
            //END CONTRATOS
            //START EQUIPO TRABAJO
            case Help.EQUIPO_TRABAJO + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM3) {
                    String nombre = msj.getParametros().get(0).trim();
                    String descripcion = msj.getParametros().get(1).trim();
                    String estado = msj.getParametros().get(2).trim();
                    Object[] responsse = NEGOCIO_EQUIPO_TRABAJO.guardar(nombre, descripcion, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EQUIPO_TRABAJO + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM4) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String nombre = msj.getParametros().get(1).trim();
                    String descripcion = msj.getParametros().get(2).trim();
                    String estado = msj.getParametros().get(3).trim();
                    Object[] responsse = NEGOCIO_EQUIPO_TRABAJO.modificar(id, nombre, descripcion, estado);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EQUIPO_TRABAJO + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_EQUIPO_TRABAJO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EQUIPO_TRABAJO + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_EQUIPO_TRABAJO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.equipoTrabajoHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.EQUIPO_TRABAJO + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_EQUIPO_TRABAJO.listar();
                list(Help.equipoTrabajoHeader, lista, msj);
                break;
            }
            //END EQUIPO TRABAJO

            //START EMPLEADO EQUIPO TRABAJO
            case Help.EMPLEADO_EQUIPO_TRABAJO + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM4) {
                    int empleado_id = Integer.parseInt(msj.getParametros().get(0).trim());
                    int equipo_trabajo_id = Integer.parseInt(msj.getParametros().get(1).trim());
                    String estado = msj.getParametros().get(2).trim();
                    String ocupacion = msj.getParametros().get(3).trim();
                    Object[] responsse = NEGOCIO_EMPLEADO_EQUIPO_TRABAJO.guardar(empleado_id, equipo_trabajo_id, estado, ocupacion);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EMPLEADO_EQUIPO_TRABAJO + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM3) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String estado = msj.getParametros().get(1).trim();
                    String ocupacion = msj.getParametros().get(2).trim();
                    Object[] responsse = NEGOCIO_EMPLEADO_EQUIPO_TRABAJO.modificar(id, estado, ocupacion);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EMPLEADO_EQUIPO_TRABAJO + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_EMPLEADO_EQUIPO_TRABAJO.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.EMPLEADO_EQUIPO_TRABAJO + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_EMPLEADO_EQUIPO_TRABAJO.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.empleadoEquipoTrabajoHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.EMPLEADO_EQUIPO_TRABAJO + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_EMPLEADO_EQUIPO_TRABAJO.listar();
                list(Help.empleadoEquipoTrabajoHeader, lista, msj);
                break;
            }
            //END EMPLEADO EQUIPO TRABAJO
            //START INCIDENCIAS
            case Help.INCIDENCIAS + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM2) {
                    String nombre = msj.getParametros().get(0).trim();
                    String descripcion = msj.getParametros().get(1).trim();
                    Object[] responsse = NEGOCIO_INCIDENCIA.guardar(nombre, descripcion);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.INCIDENCIAS + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM3) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String nombre = msj.getParametros().get(1).trim();
                    String descripcion = msj.getParametros().get(2).trim();
                    Object[] responsse = NEGOCIO_INCIDENCIA.modificar(id, nombre, descripcion);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.INCIDENCIAS + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_INCIDENCIA.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.INCIDENCIAS + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_INCIDENCIA.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.incidenciasHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.INCIDENCIAS + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_INCIDENCIA.listar();
                list(Help.incidenciasHeader, lista, msj);
                break;
            }
            // END INCIDENCIAS
                //START CONTRATO INCIDENCIAS
            case Help.CONTRATO_INCIDENCIA + "_" + Help.ADD: {
                if (msj.getParametros().size() == Help.LENPARAM3) {
                    int contrato_id = Integer.parseInt(msj.getParametros().get(0).trim());
                    int incidencia_id = Integer.parseInt(msj.getParametros().get(1).trim());
                    String estado = msj.getParametros().get(2).trim();
                    String fecha_solucion = msj.getParametros().get(3).trim();
                    // Formateador para el formato yyyy-MM-dd
                    DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    // Parsear String a LocalDate
                    LocalDate localDate_solucion = LocalDate.parse(fecha_solucion, date_formatter);
                    // Convertir LocalDate a LocalDateTime (agregando hora 00:00)
                    LocalDateTime fecha_solucion_convertida = localDate_solucion.atStartOfDay();
                    Object[] responsse = NEGOCIO_CONTRATO_INCIDENCIA.guardar(contrato_id, incidencia_id, estado, fecha_solucion_convertida);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CONTRATO_INCIDENCIA + "_" + Help.MOD: {
                if (msj.getParametros().size() == Help.LENPARAM4) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String estado = msj.getParametros().get(1).trim();
                    String fecha_solucion = msj.getParametros().get(2).trim();
                    // Formateador para el formato yyyy-MM-dd
                    DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    // Parsear String a LocalDate
                    LocalDate localDate_solucion = LocalDate.parse(fecha_solucion, date_formatter);
                    // Convertir LocalDate a LocalDateTime (agregando hora 00:00)
                    LocalDateTime fecha_solucion_convertida = localDate_solucion.atStartOfDay();
                    Object[] responsse = NEGOCIO_CONTRATO_INCIDENCIA.modificar(id, estado, fecha_solucion_convertida);
                    String message = (String) responsse[1];
                    sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CONTRATO_INCIDENCIA + "_" + Help.DEL: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    Object[] responsse = NEGOCIO_CONTRATO_INCIDENCIA.eliminar(id);
                    String message = (String) responsse[1];
                    if (responsse[0] == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                    } else {
                        sendMail(msj.getEmisor(), msj.evento(), message.toUpperCase());
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            case Help.CONTRATO_INCIDENCIA + "_" + Help.VER: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String[] data = NEGOCIO_CONTRATO_INCIDENCIA.ver(id);
                    //sendMail(msj.getEmisor(), msj.evento(), "El dato de la tabla " + msj.getTable());
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCONTRO NINGUN DATO CON ESTE ID: " + id);
                        break;
                    } else {
                        ver(Help.contratoIncidenciaHeader, data, msj);
                        break;
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                    break;
                }
            }
            case Help.CONTRATO_INCIDENCIA + "_" + Help.LIS: {
                List<String[]> lista = NEGOCIO_CONTRATO_INCIDENCIA.listar();
                list(Help.contratoIncidenciaHeader, lista, msj);
                break;
            }
            // START REPORTES
            case Help.CONTRATOS + "_" + Help.REP: {
                if (msj.getParametros().size() == Help.LENPARAM1) {
                    int id = Integer.parseInt(msj.getParametros().get(0).trim());
                    String name_pdf = Help.PATH + "CONTRATO_"+id+"_"+horaFormateada+".pdf";
                    //String chart_image = Help.PATH + "PRODUCTO_VENDIDO_" + horaFormateada + ".png";
                    String[] data = NEGOCIO_REPORTE.contrato(id, name_pdf);
                    System.out.println("respuesta desde negocio: "+ Arrays.toString(data));
                    if (data == null) {
                        sendMail(msj.getEmisor(), msj.evento(), "NO SE ENCUENTRAN DATOS REGISTRADOS PARA ESTA CONSULTA".toUpperCase());
                    } else {
                        verWithReport(data, msj, name_pdf);
                    }
                } else {
                    sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, erorrLengthParametros(msj));
                }
                break;
            }
            //REPORTES
            //END REPORTES
            default:
                sendMail(msj.getEmisor(), PARAMETROS_INCORRECTOS, tablaOActionNotFount(msj));
                break;
        }
    }
    private String errorConexion(String exception, Mensaje msj) {
        return Help.errorMensaje("Error de conexión", "Error al conectarse al servidor, intente nuevamente.",
                exception + " No se pudo ejecutar el comando: " + msj.toString());
    }
    private String erorrLengthParametros(Mensaje msj) {
        return Help.errorMensaje("Error Parametros", "Cantidad de parametros incorrectos.", msj.toString());
    }
    private String errorConvertirParametros(Mensaje msj) {
        return Help.errorMensaje("Error convercion", "Error al convertir elementos del parametro", msj.toString());
        //return "ERROR AL CONVERTIR PARAMETROS: \n" + msj.toString();
    }
    private String comandoIncorrecto(String token) {
        return Help.errorMensaje("Error de comandos", "No se pudo decifrar el comando", " comando: " + token);
        //return "No se pudo decifrar el comando: \n" + token + "\n PARA MAS AYUDA ENVIE UN CORREO CON LA PALABRA \t HELP \t";
    }
    private String tablaOActionNotFount(Mensaje msj) {
        return Help.errorMensaje("Error TABLA o ACCCION", "Tabla O Accion no encontrada", msj.toString());

        //return "!!!!TABLA O ACCION NO ENCONTRADA!!! \n Error en el comando: \n" + msj.toString() +"\n PARA MAS AYUDA ENVIE UN CORREO CON LA PALABRA \t HELP\t!!!!";
    }
    private void list(String[] header, List<String[]> lista, Mensaje msj) throws IOException {
        String body = Help.listMensaje(msj.tableAction(), header, lista);
        sendMail(msj.getEmisor(), msj.evento(), body);
    }
    private void listWithReport(String[] header, List<String[]> lista, Mensaje msj, String name_pdfFile) throws IOException {
        String body = Help.listMensajeToPdf(msj.tableAction(), header, lista);
        sendMailPdf(msj.getEmisor(), msj.evento(), body, name_pdfFile);
    }
    private void verWithReport(String[] data, Mensaje msj, String name_pdfFile) throws IOException {
        String body = Help.verWithReport(msj.tableAction(), data);
        System.out.println("cuerpo en el viewReport");
        System.out.println(body);
        sendMailPdf(msj.getEmisor(), msj.evento(), body, name_pdfFile);
    }
    private void ver(String[] header, String[] cu, Mensaje msj) throws IOException {
        String body = Help.ver(msj.tableAction(), header, cu);
        sendMail(msj.getEmisor(), msj.evento(), body);
    }
    private void sendMail(String rcpt, String titulo, String mensaje) throws IOException {
        System.out.println("rcpt: " + rcpt + " , titulo: " + titulo + " , mensaje: " + mensaje);
        Smtp smtp = new Smtp(ConstGlobal.SERVIDOR, ConstGlobal.PORT_SMPT);
        smtp.sendMail(ConstGlobal.EMAIL, "<" + rcpt + ">", titulo, mensaje);
    }
    private void sendMailPdf(String rcpt, String titulo, String mensaje, String name_pdfFile) throws IOException {
        System.out.println("rcpt: " + rcpt + "titulo: " + titulo + "mensaje: " + mensaje);
        Smtp smtp = new Smtp(ConstGlobal.SERVIDOR, ConstGlobal.PORT_SMPT);
        smtp.sendMailWithPdf(ConstGlobal.EMAIL, "<" + rcpt + ">", titulo, mensaje, name_pdfFile);
    }
}
