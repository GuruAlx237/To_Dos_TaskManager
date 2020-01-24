package com.example.to_dos_taskmanager.DataBase_TD;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.to_dos_taskmanager.DataBase_TD.Librerias.Auxiliares;

public class DB_To_Dos extends SQLiteOpenHelper {

    private static final String nom_DB = "to_dos";
    private static final int ver_DB = 1;

    //region Tablas
    public static class TAB_Tareas{

        //region Atributos Clase
        public static final String Nombre = "Tareas";

        public static enum Campos{
            ID("Nombre", "TEXT", "PRIMARY KEY NOT NULL"),
            FECHA("Fecha", "TEXT", "NOT NULL"),
            DESCRIPCION("Descripcion", "TEXT", "NOT NULL");

            final String nom,tpo,mod;

            Campos(String nom, String tpo, String mod) {
                this.nom = nom;
                this.tpo = tpo;
                this.mod = mod;
            }

            //region GETTER
            public String getNom() {
                return nom;
            }

            public String getTpo() {
                return tpo;
            }

            public String getMod() {
                return mod;
            }
            //endregion

            @Override
            public String toString() {
                return nom;
            }
        }

        String id_nombre,fecha,descripcion;
        //endregion


        public TAB_Tareas(String id_nombre, String fecha, String descripcion) {
            this.id_nombre = id_nombre;
            this.fecha = fecha;
            this.descripcion = descripcion;
        }

        //region Get&Set
        public static String getNombre() {
            return Nombre;
        }

        public String getId_nombre() {
            return id_nombre;
        }

        public void setId_nombre(String id_nombre) {
            this.id_nombre = id_nombre;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
        //endregion
    }
    //endregion

    //region Operaciones para BD
    public static boolean bol_Insertar(Object obj,boolean toast, Context ctx){

        String men="";
        boolean res = false;
        long r = -1;

        DB_To_Dos con = new DB_To_Dos(ctx);
        SQLiteDatabase wdb = con.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String tab = "",ele = "";

        if(obj.getClass().equals(TAB_Tareas.class)){

            TAB_Tareas ed = (TAB_Tareas)(obj);

            cv.put(TAB_Tareas.Campos.ID.nom,ed.getId_nombre());
            cv.put(TAB_Tareas.Campos.FECHA.nom,ed.getFecha());
            cv.put(TAB_Tareas.Campos.DESCRIPCION.nom,ed.getDescripcion());

            tab = TAB_Tareas.Nombre;
            ele = ed.getId_nombre();
        }
        else {
            Log.d(ContentValues.TAG, "pro_Insertar: Alx: NO es");
        }

        try {
            r = wdb.insertOrThrow(tab,null,cv);
            wdb.close();

            men = ele + " agregado a " + tab;
            if (toast) {
                Toast.makeText(ctx, men, Toast.LENGTH_SHORT).show();
            }
            System.out.println("Insert: " + men );
        }
        catch (Exception e) {
            e.printStackTrace();
            men = (e.getMessage().contains("UNIQUE")?"Ya existe "+ ele + " en " + tab:"No fue posible agregar el elemento");
            if (toast) {
                Toast.makeText(ctx, men + " error copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
            men = "Insert: " + men + " desde "  + tab + " - Error: " + e.getMessage();
            Auxiliares.pro_MenToClip(men,ctx);
            r =  -1;
        }

        return res;
    }

    public static Cursor cur_Select(String campos, String tabla, String condicion, boolean toast, Context ctx){
        Cursor cur = null;
        String sql_cmd = "", men="";

        if(condicion == "MAX"){
            condicion = String.format("Id = (SELECT MAX(Id)  FROM %s)",tabla);
        }

        if(condicion != ""){
            condicion = " WHERE " + condicion;
        }



        try {
            sql_cmd = String.format("SELECT %s FROM %s %s;", campos, tabla, condicion);
            DB_To_Dos con = new DB_To_Dos(ctx);
            SQLiteDatabase rdb = con.getReadableDatabase();

            cur = rdb.rawQuery(sql_cmd,null);
            men = "Consulta realizada, " + cur.getCount() + " resultados";

            rdb.close();


            if (toast) {
                Toast.makeText(ctx, men, Toast.LENGTH_SHORT).show();
            }
            System.out.println("Select: " + men + " desde "  + tabla + " con la condición: " + condicion);
        }
        catch (Exception e) {
            e.printStackTrace();
            men = "Problemas al realizar consulta";
            if (toast) {
                Toast.makeText(ctx, men + " error copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
            men = "Select: " + men + " desde "  + tabla + " con la condición: " + condicion + " - Error: " + e.getMessage();
            Auxiliares.pro_MenToClip(men,ctx);
            cur = new Cursor() {
                @Override
                public int getCount() {
                    return 0;
                }

                @Override
                public int getPosition() {
                    return 0;
                }

                @Override
                public boolean move(int offset) {
                    return false;
                }

                @Override
                public boolean moveToPosition(int position) {
                    return false;
                }

                @Override
                public boolean moveToFirst() {
                    return false;
                }

                @Override
                public boolean moveToLast() {
                    return false;
                }

                @Override
                public boolean moveToNext() {
                    return false;
                }

                @Override
                public boolean moveToPrevious() {
                    return false;
                }

                @Override
                public boolean isFirst() {
                    return false;
                }

                @Override
                public boolean isLast() {
                    return false;
                }

                @Override
                public boolean isBeforeFirst() {
                    return false;
                }

                @Override
                public boolean isAfterLast() {
                    return false;
                }

                @Override
                public int getColumnIndex(String columnName) {
                    return 0;
                }

                @Override
                public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                    return 0;
                }

                @Override
                public String getColumnName(int columnIndex) {
                    return null;
                }

                @Override
                public String[] getColumnNames() {
                    return new String[0];
                }

                @Override
                public int getColumnCount() {
                    return 0;
                }

                @Override
                public byte[] getBlob(int columnIndex) {
                    return new byte[0];
                }

                @Override
                public String getString(int columnIndex) {
                    return null;
                }

                @Override
                public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

                }

                @Override
                public short getShort(int columnIndex) {
                    return 0;
                }

                @Override
                public int getInt(int columnIndex) {
                    return 0;
                }

                @Override
                public long getLong(int columnIndex) {
                    return 0;
                }

                @Override
                public float getFloat(int columnIndex) {
                    return 0;
                }

                @Override
                public double getDouble(int columnIndex) {
                    return 0;
                }

                @Override
                public int getType(int columnIndex) {
                    return 0;
                }

                @Override
                public boolean isNull(int columnIndex) {
                    return false;
                }

                @Override
                public void deactivate() {

                }

                @Override
                public boolean requery() {
                    return false;
                }

                @Override
                public void close() {

                }

                @Override
                public boolean isClosed() {
                    return false;
                }

                @Override
                public void registerContentObserver(ContentObserver observer) {

                }

                @Override
                public void unregisterContentObserver(ContentObserver observer) {

                }

                @Override
                public void registerDataSetObserver(DataSetObserver observer) {

                }

                @Override
                public void unregisterDataSetObserver(DataSetObserver observer) {

                }

                @Override
                public void setNotificationUri(ContentResolver cr, Uri uri) {

                }

                @Override
                public Uri getNotificationUri() {
                    return null;
                }

                @Override
                public boolean getWantsAllOnMoveCalls() {
                    return false;
                }

                @Override
                public void setExtras(Bundle extras) {

                }

                @Override
                public Bundle getExtras() {
                    return null;
                }

                @Override
                public Bundle respond(Bundle extras) {
                    return null;
                }
            };
        }

        return  cur;
    }

    public static int int_Update(Object obj,String cond,boolean toast, Context ctx){

        int r;
        String men;

        DB_To_Dos con = new DB_To_Dos(ctx);
        SQLiteDatabase wdb = con.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String tab = "",ele = "";

        if(obj.getClass().equals(TAB_Tareas.class)){

            TAB_Tareas ed = (TAB_Tareas)(obj);

            cv.put(TAB_Tareas.Campos.ID.nom,ed.getId_nombre());
            cv.put(TAB_Tareas.Campos.FECHA.nom,ed.getFecha());
            cv.put(TAB_Tareas.Campos.DESCRIPCION.nom,ed.getDescripcion());

            tab = TAB_Tareas.Nombre;
            ele = ed.getId_nombre();
        }
        else {
            Log.d(ContentValues.TAG, "pro_Insertar: Alx: NO es");
        }


        try {
            r = wdb.update(tab,cv, cond,null);
            wdb.close();

            men = (r>1?"Elemendos modificados":(r==1?"Elemendo modificado":"No se encontraron elementos dentro de esa condición"));
            if (toast) {
                Toast.makeText(ctx, men, Toast.LENGTH_SHORT).show();
            }
            System.out.println("Update: " + men + " desde "  + tab + " con la condición: " + cond);
        }
        catch (Exception e) {
            e.printStackTrace();
            men = (e.getMessage().contains("UNIQUE")?"Ya existe "+ ele + " en " + tab:"No fue posible modificar el elemento");
            if (toast) {
                Toast.makeText(ctx, men + " error copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
            men = "Update: " + men + " desde "  + tab + " con la condición: " + cond + " - Error: " + e.getMessage();
            Auxiliares.pro_MenToClip(men,ctx);
            r =  -1;
        }

        return r;
    }

    public static int int_Delete(String tabla,String cond,boolean toast, Context ctx){

        int r;
        String men = "";

        DB_To_Dos con = new DB_To_Dos(ctx);
        SQLiteDatabase wdb = con.getWritableDatabase();

        try {
            r = wdb.delete(tabla, cond,null);
            wdb.close();
            men = (r>1?"Elementos eliminados":(r==1?"Elemento eliminado":"No se encontraron elementos dentro de esa condición"));
            if (toast) {
                Toast.makeText(ctx, men, Toast.LENGTH_SHORT).show();
            }
            System.out.println("Delete: " + men + " desde "  + tabla + " con la condición: " + cond);
        }
        catch (Exception e) {

            men = "No fue posible eliminar el elemento";
            if (toast) {
                Toast.makeText(ctx, men + " error copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }

            men  = "Delete: " + men + " desde "  + tabla + " con la condición: " + cond + " - Error: " + e.getMessage();
            Auxiliares.pro_MenToClip(men,ctx);

            r =  -1;
        }

        return r;
    }
    //endregion

    public DB_To_Dos(Context context) {
        super(context, nom_DB+".db", null, ver_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String s_tab = "", s_cam = "";  //Variables para almacenar cadena de creación de tabla

        //region Dipositivos
        TAB_Tareas.Campos cd;
        s_cam = "";
        s_tab = "CREATE TABLE " + TAB_Tareas.Nombre + " (";
        for (int i = 0; i < TAB_Tareas.Campos.values().length; i++) {
            cd  = TAB_Tareas.Campos.values()[i];
            s_cam += cd.getNom() + " " + cd.getTpo() + " " + cd.getMod();
            if (i < TAB_Tareas.Campos.values().length - 1) {
                s_cam += ", ";
            }
        }
        s_tab += s_cam + ");";

        db.execSQL(s_tab);
        //endregion
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>1){
            //Comandos para actualizar tablas
        }

        if(newVersion>2){
            //Comandos para actualizar tablas
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen (SQLiteDatabase bd)
    {
        super.onOpen(bd);

        if(!bd.isReadOnly())
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                bd.setForeignKeyConstraintsEnabled(true);
            } else {
                bd.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }
}
