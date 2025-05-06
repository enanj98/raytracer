package raytracer_jahic;

public class Vector3 {
	float x = 0,y = 0,z = 0;

public Vector3(float x, float y, float z) {
	super();
	this.x = x;
	this.y = y;
	this.z = z;
}
public Vector3() { 				// default constructor creates unit vector
	super();
	this.x = 1;
	this.y = 1;
	this.z = 1;
}
 
public float dot(Vector3 vector) {
	return this.x * vector.x + this.y * vector.y  + this.z * vector.z;
	
}

/*
 formula used from : https://en.wikipedia.org/wiki/Cross_product 
 start 30 - end 33
 */
public Vector3 cross(Vector3 vector) {
	return new Vector3(
			this.y* vector.z - this.z * vector.y,
			this.z* vector.x - this.x * vector.z,
			this.x * vector.y - this.y * vector.y
			);
	
} 
public Vector3 timesScalar(int scalar) {
	return new Vector3(this.x * scalar,this.y * scalar, this.z * scalar);
} 
public Vector3 timesScalarfloat(float scalar) {

	 return new Vector3(this.x * scalar, this.y* scalar, this.z * scalar);
} 
public Vector3 plus(Vector3 vector) {
	
	
	 return new Vector3(this.x + vector.x, this.y+ vector.y, this.z + vector.z);
	
}
public Vector3 plus(float value) {
	
	
	 return new Vector3(this.x + value, this.y+ value, this.z + value);
	
}
public Vector3 plusLimit(Vector3 vector) {
	
	 return new Vector3(this.x + vector.x, this.y + vector.y, this.z + vector.z);
	
} 
public Vector3 minus(Vector3 vector) {
	return new Vector3(this.x - vector.x,this.y - vector.y,this.z - vector.z);
	
} 
public Vector3 times(Vector3 vector) {
	return new Vector3(this.x * vector.x,this.y * vector.y,this.z * vector.z);
	
} 
/*
 * http://www.fundza.com/vectors/normalize/#:~:text=Normalizing%20a%20vector%20involves%20two,xyz)%20components%20by%20its%20length.
 * Formula taken from :
 * Line 74-75
 */
public Vector3 normalize() {
	float length = (float) Math.sqrt(x*x + y*y + z*z);
	return new Vector3(this.x * (1.0f / length),this.y * (1.0f /length),this.z * (1.0f / length));
}

public float getX() {
	return x;
}

public float getY() {
	return y;
}

public float getZ() {
	return z;
}
	
	
	
}
