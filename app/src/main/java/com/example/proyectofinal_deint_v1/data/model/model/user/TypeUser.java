package com.example.proyectofinal_deint_v1.data.model.model.user;
/*
Existen tres tipos de usuarios en la aplicación -->
    - Admin : usuario que tendrá el total acceso de la aplicación, repositorios, datos de usuarios,etc...
    - Normal : usuario que podrá acceder a las funciones de la aplicación, pero no tendrá los permisos de modificado de base de datos,etc...
    - Coach : igual que el usuario normal, pero tendrá la opción de solicitar acceso a otra cuenta de un usuario normal, para poder colaborar con
    este, añadiendo rutinas, recordatorios, objetivos,etc... Podrá chatear también con el cliente,etc... Bajo la restricción de que previamente, el cliente
    haya aceptado la solicitud del coach como colaborador.
 */
public class TypeUser {
    public static final int ADMIN = 0;
    public static final int NORMAL = 1;
    public static final int COACH = 2;
}
