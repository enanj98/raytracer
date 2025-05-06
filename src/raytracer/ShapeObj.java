package raytracer_jahic;

import java.awt.Color;

public class ShapeObj {
	
	float radius;
	float xPos,yPos,zPos;
	int  color;
	float ka,ks,kd;
	float exponent;
	 int getColorInt(float r, float g, float b) {
		return new Color(r,g,b).getRGB();
	}
	public ShapeObj(float radius, float xPos, float yPos, float zPos, float r,float g,float b) {
		super();
		this.radius = radius;
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.color = getColorInt(r,g,b);
	}
	public ShapeObj() {
		
	}
	public void setColorrgb(float r,float g,float b) {
		this.color = getColorInt(r,g,b);
	}
	

	public void setRadius(float radius) {
		this.radius = radius;
	}
	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
	public void setxPos(float xPos) {
		this.xPos = xPos;
	}
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}
	public void setzPos(float zPos) {
		this.zPos = zPos;
	}
	public void setColor(int color) {
		this.color = color;
	}

	public void setKa(float ka) {
		this.ka = ka;
	}
	public void setKs(float ks) {
		this.ks = ks;
	}
	public void setKd(float kd) {
		this.kd = kd;
	}
	
	/*
	 * 
	 * Taken from:
	 * https://iquilezles.org/articles/intersectors/
	 *  
	 *  Start 68 - end 81
	 */
	
	public float checkIntersect(Vector3 rayOrigin,Vector3 rayDirection,Vector3 centerPos,float radius) {
		
		    Vector3 oc = rayOrigin.minus(centerPos); // ray to pos
		    float b = oc.dot(rayDirection);
		    float c = oc.dot(oc) - radius*radius;
		    float h = b*b - c;
		    if( h<0.0f ) return -1.0f; // no intersection
		    h =(float) Math.sqrt(h);
		   
		    return -b - h;//distance to intersection
		    
		    

	}
	
	
	
	
	/*
	 * 
	 * Did the same way as explained on Tutorial slides page 12
	 * Line 94-94
	 */
	public Vector3 getNormal(Vector3 rayOrigin,Vector3 ce,float h,Vector3 rayDir) {
		
		  //passing value from intersection test,and normalized vector to center of sphere is our normal
		 Vector3 normal = rayOrigin.plus(rayDir.timesScalarfloat(h)).minus(ce).normalize(); 
		 
		 

		 normal.normalize();		
		 return normal;
		 
		 
	
	}
	
	
	/*
	
	 Formula for reflection taken from:https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/reflect.xhtml Line 121-121
	*  used code from my own lab 1b submission line 111-133
	*/											
	public Vector3 shadePhong(Vector3 cameraPos, Vector3 lightPos,Vector3 worldPos,Vector3 v_normal, Vector3 vertColor) {
	       
           
	       Vector3 lightVector = lightPos.normalize();  //vector to the light L 
	       v_normal.normalize();
	       
	       
	       							
	       Vector3 eye = new Vector3(-worldPos.x,-worldPos.y,1.0f).normalize(); //E
         
           Vector3 reflectionDirection = new Vector3(-lightVector.x,-lightVector.y,-lightVector.z).normalize().minus(v_normal.timesScalarfloat(2.0f*(v_normal.dot(new Vector3(-lightVector.x,-lightVector.y,-lightVector.z).normalize()))));
          
           Vector3 lightIntensity = vertColor.timesScalarfloat((Math.max(v_normal.dot(lightVector),0.0f))).timesScalarfloat(this.kd);
         
           float specular;
           if(lightVector.dot(v_normal) > 0.0){
        	   specular =  (float)Math.pow(Math.max(reflectionDirection.normalize().dot(eye),0.0),this.exponent);
           }else {
        	   specular = 0.0f;
           }
           
           		   //Vec3                    Vec3 ambient                               float
           return (lightIntensity.plus(vertColor.timesScalarfloat(this.ka))).plus(new Vector3(this.ks*255.0f,this.ks*255.0f,this.ks*255.0f).timesScalarfloat(specular));
         
         
	}
	public float getRadius() {
		return radius;
	}
	public float getxPos() {
		return xPos;
	}
	public float getyPos() {
		return yPos;
	}
	public float getzPos() {
		return zPos;
	}
	public int getColor() {
		return color;
	}
	public Vector3 getxyz() {
		return new Vector3(this.xPos,this.yPos,this.zPos);
	}
	public Color getRGB() {
		return new Color(this.color,true);
	
	}

	
	

}
