package math;

/**
 * Esta clase se instancia y se utiliza para realizar el movimiento de los objetos
 * tales como el Player,Meteor,Ufo.
 * Contiene metodos que realizan operaciones entre vectores.
 */
public class Vector2D {
	private double x,y;
	/**
	 constructor con parametros
	 @param x representa la primer componente del vector
	 @param y representa la segunda componente del vector
	 */
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	/**
	 constructor inicializado con un vector de parametro
	 @param v vector el cual se quiere clonar
	 */
	public Vector2D(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}
	/**
	 constructor por defecto inicializa x e y en 0
	 */
	public Vector2D()
	{
		x = 0;
		y = 0;
	}

	/**
	 suma vectores componente a componente
	 @param  v Vector el cual se le quiere sumar
	 @return retorna un nuevo vector que es la suma del vector actual y del que recibe como parametro
	 */
	public Vector2D add(Vector2D v)
	{
		return new Vector2D(x + v.getX(), y + v.getY());
	}

	/**
	 resta vectores componente a componente
	 @param  v Vector el cual se le quiere restar
	 @return retorna un nuevo vector que contiente el vector actual menos el que recibe como parametro
	 */
	public Vector2D subtract(Vector2D v)
	{
		return new Vector2D(x - v.getX(), y - v.getY());
	}

	/**
	 multiplica el vector por una constante para escalarlo
	 @param  value numero por el cual se desea multiplicar
	 @return retorna un nuevo vector en el cual sus parametros son los del vector actual multiplicados por value
	 */
	public Vector2D scale(double value)
	{
		return new Vector2D(x*value, y*value);
	}

	/**
	 establece el limite maximo del vector actual
	 @param  value maximo valor de maginitud admitido para el vector
	 @return se retorna a si mismo con las componentes de su vector seteadas al maximo valor
	 */
	public Vector2D limit(double value)
	{
		if(getMagnitude() > value)
		{
			return this.normalize().scale(value);
		}
		return this;
	}

	/**
	 Toma el vector actual y lo normaliza haciendo su modulo igual a 1
	 @return retorna un nuevo vector con los parametros normalizados
	 */
	public Vector2D normalize()
	{
		double magnitude = getMagnitude();
		
		return new Vector2D(x / magnitude, y / magnitude);
	}

	/**
	 Usa pitagoras para sacar el modulo del vector actual
	 @return retorna la magnitud o modulo del vector
	 */
	public double getMagnitude()
	{
		return Math.sqrt(x*x + y*y);
	}

	/**
	 Su funcion es cambiar la direccion del vector obteniendo la magnitud actual y
	 descomponiendola mediante el angulo pasado como argumento
	 @param angle angulo actual
	 @return retorna un nuevo vector el cual mantiene su magnitud pero cambia sus parametros x e y con angle
	 */
	public Vector2D setDirection(double angle)
	{
		double magnitude = getMagnitude();
		
		return new Vector2D(Math.cos(angle)*magnitude, Math.sin(angle)*magnitude);
	}

	/**
	 Calcula y devuelve el angulo actual del vector mediante identidades trigonometricas
	 Sen = C0 / H = y/magnitud
	 angle = arsen(y/magnitud)
	 @return retorna una variable double con el valor del angulo del vector actual
	 */
	public double getAngle() {
		return Math.asin(y/getMagnitude());
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
}
