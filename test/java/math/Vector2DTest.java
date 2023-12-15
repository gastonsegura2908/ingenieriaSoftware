package math;

import org.junit.jupiter.api.Test;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

    @Test
    void add() {
        Vector2D vector1 = new Vector2D(1,5);
        Vector2D vector2 = new Vector2D(3,8);
        Vector2D suma = vector1.add(vector2);

        assertEquals(4,suma.getX());
        assertEquals(13,suma.getY());

        vector1.setY(0);
        vector1.setX(0);
        vector2.setY(0);
        vector2.setX(0);
        suma = vector1.add(vector2);
        assertEquals(0,suma.getX());
        assertEquals(0,suma.getY());

    }

    @Test
    void subtract() {
        Vector2D vector1 = new Vector2D(1,5);
        Vector2D vector2 = new Vector2D(3,8);
        Vector2D resta = vector1.subtract(vector2);

        assertEquals(-2,resta.getX());
        assertEquals(-3,resta.getY());

        vector1.setY(0);
        vector1.setX(0);
        vector2.setY(0);
        vector2.setX(0);
        resta = vector1.subtract(vector2);
        assertEquals(0,resta.getX());
        assertEquals(0,resta.getY());
    }


    @Test
    void limit() {
        // al superar los limites se espera que se escale al limite
        Vector2D vector_unitario = new Vector2D(1,1).normalize();

        double limit = 8;   // limite

        // VECTOR POR DEBAJO DEL LIMITE, NO DEBE CAMBIAR

        double mag1 = 4;    // magnitud del primer vector
        Vector2D vector1 = vector_unitario.scale(mag1);

        // round double
        double mag1_after_limit = Math.round(vector1.limit(limit).getMagnitude());
        assertEquals(mag1,mag1_after_limit);

        // VECTOR POR ENCIMA DEL LIMITE, DEBE ESCALARSE AL LIMITE

        double mag2 = 10;   // magnitud del segundo vector, por encima del limite
        Vector2D vector2 = vector_unitario.scale(mag2);
        double mag2_after_limit = Math.round(vector2.limit(limit).getMagnitude());
        assertEquals(limit,mag2_after_limit);
    }

    @Test
    void normalize() {
        // se espera que se cree un vector de magnitud unitaria
        Vector2D vector_unitario = new Vector2D(0,1);

        // el vector unitario no debe cambiar al normalizarlo
        assertEquals(vector_unitario.getMagnitude(),vector_unitario.normalize().getMagnitude());

        // VECTORES DE MAGNITUDES NO UNITARIAS
        double mag1 = 4;    // magnitud del primer vector
        Vector2D vector1 = vector_unitario.scale(mag1);

        Vector2D normalized = vector1.normalize();

        assertNotEquals(vector1.getMagnitude(), normalized.getMagnitude());
        assertEquals(1, normalized.getMagnitude());
        assertEquals(vector1.getX()/mag1, normalized.getX());
        assertEquals(vector1.getY()/mag1, normalized.getY());

        double mag2 = 25;    // magnitud del segundo vector
        Vector2D vector2 = new Vector2D(1/sqrt(2),1/sqrt(2)).scale(mag2);

        Vector2D normalized2 = vector2.normalize();

        assertNotEquals(vector2.getMagnitude(), normalized2.getMagnitude());
        assertEquals(1, Math.round(normalized2.getMagnitude()));
        assertEquals(vector2.getX()/mag2, normalized2.getX());
        assertEquals(vector2.getY()/mag2, normalized2.getY());

    }

    @Test
    void getMagnitude() {
        // se crea un vector y se verifica su magnitud
        Vector2D vector1 = new Vector2D(1,4);
        double mag = vector1.getMagnitude();
        assertEquals(4.123105625617661,mag);

        // otro vector
        double x = 3;
        double y = 8;
        Vector2D vector2 = new Vector2D(x, y);
        double mag2 = vector2.getMagnitude();
        assertEquals(sqrt(x*x + y*y),mag2);
    }

    @Test
    void setDirection() {
        // se crea un vector y se verifica su angulo
        Vector2D vector1 = new Vector2D(1,4);
        double angulo = vector1.getAngle();
        assertEquals(1.3258176636680323,angulo);

        // se cambia el angulo del vector
        assertEquals(0,vector1.setDirection(0).getAngle());

        // otro vector
        double x = 3;
        double y = 8;
        Vector2D vector2 = new Vector2D(x, y);
        double angulo2 = vector2.getAngle();
        assertEquals(Math.asin(y/vector2.getMagnitude()),angulo2);

        // se cambia el angulo del vector
        assertEquals(Math.PI/2,vector2.setDirection(Math.PI/2).getAngle());
    }

    @Test
    void getAngle() {
        // se crea un vector y se verifica su angulo
        Vector2D vector1 = new Vector2D(1,4);
        double angulo = vector1.getAngle();
        assertEquals(1.3258176636680323,angulo);

        // otro vector
        double x = 3;
        double y = 8;
        Vector2D vector2 = new Vector2D(x, y);
        double angulo2 = vector2.getAngle();
        assertEquals(Math.asin(y/vector2.getMagnitude()),angulo2);
    }

    @Test
    void scale() {
        // se escala un vector por determinado valor (multiplicar componente a componente)
        Vector2D vector1 = new Vector2D(1,4);
        double valor = 25;
        Vector2D result = vector1.scale(valor);

        assertEquals(25,result.getX());
        assertEquals(100,result.getY());

    }
}