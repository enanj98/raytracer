package raytracer_jahic;

import java.awt.image.BufferedImage;
import java.awt.Color;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;







public class Main {

	public static void main(String[] args) throws SAXException, IOException {
		
		
		
		
		
		

		
		
		
		
		Scanner cin = new Scanner(System.in);
		System.out.println("Please enter Path from readmefile:");
		String path = cin.nextLine();
		cin.close();
		/*
		 * https://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilder.html
		 * Line 50-53
		 */
		File file = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		ShapeObj[]shapes =  new ShapeObj[ doc.getElementsByTagName("sphere").getLength()];  /// make array of size of amount of spheres ;
		Vector3 lightPos = new Vector3(3,-3,1); // only hardcoded it here bcs example1.xml doesnt have a lightpos for other ex2.xml and x3.xml it gets parsed
		Vector3 cameraPos = new Vector3();	
		// parse XML and save data into ShapeObj
			NodeList filename = doc.getElementsByTagName("scene");
			String FileOutputName = filename.item(0).getAttributes().item(0).getNodeValue(); // takes value from xml file for a filename
			NodeList resolutionList = doc.getElementsByTagName("resolution");
			int resolutionH = Integer.parseInt(resolutionList.item(0).getAttributes().item(0).getNodeValue());
			int resolutionW = Integer.parseInt(resolutionList.item(0).getAttributes().item(1).getNodeValue());
			NodeList backGroundcList = doc.getElementsByTagName("background_color");
			
			Vector3 backGcolor = new Vector3(Float.parseFloat(backGroundcList.item(0).getAttributes().item(0).getNodeValue()),Float.parseFloat(backGroundcList.item(0).getAttributes().item(1).getNodeValue()),Float.parseFloat(backGroundcList.item(0).getAttributes().item(2).getNodeValue()));
			NodeList cameraList = doc.getElementsByTagName("position");
			for(int posc = 0;posc < cameraList.getLength();posc++) {
				Node posNode = cameraList.item(posc);
				if (posNode.getNodeType() == Node.ELEMENT_NODE) {
				Element posElCamera = (Element) posNode;
				
				if(posElCamera.getParentNode().getNodeName() == "camera") {
					System.out.println(posElCamera.getParentNode().getNodeName());
					cameraPos.x = Float.parseFloat(posElCamera.getAttribute("x"));
					cameraPos.y = Float.parseFloat(posElCamera.getAttribute("y"));
					cameraPos.z = Float.parseFloat(posElCamera.getAttribute("z"));
					
				}
				
				}
				
			
			}
			NodeList sphereList = doc.getElementsByTagName("sphere");
			NodeList directionList = doc.getElementsByTagName("direction");
			for(int pos = 0;pos < directionList.getLength();pos++) {
				Node lightNode = directionList.item(pos);
				Element lightEl = (Element) lightNode;
				lightPos.x = -Float.parseFloat(lightEl.getAttribute("x")); 
				lightPos.y = -Float.parseFloat(lightEl.getAttribute("y")); 
				lightPos.z = -Float.parseFloat(lightEl.getAttribute("z")); 
			
			
			}
			for (int i = 0; i < sphereList.getLength(); i++) {
				Node value = sphereList.item(i);
				shapes[i] = new ShapeObj();
				if (value.getNodeType() == Node.ELEMENT_NODE) {
					Element sphereEl = (Element) value;
					shapes[i].setRadius(Float.parseFloat(sphereEl.getAttribute("radius")));
					System.out.println(sphereEl.getAttribute("direction"));
					for (int j = 0; j < sphereEl.getChildNodes().getLength(); j++) {
						Node coordinates = sphereEl.getChildNodes().item(j);
						if (coordinates.getNodeType() == Node.ELEMENT_NODE) {
							Element coordinateElement = (Element) coordinates;

							if (coordinateElement.getAttribute("x").length() != 0)
								shapes[i].setxPos(Float.parseFloat(coordinateElement.getAttribute("x")));
							if (coordinateElement.getAttribute("y").length() != 0)
								shapes[i].setyPos(Float.parseFloat(coordinateElement.getAttribute("y")));
							if (coordinateElement.getAttribute("z").length() != 0)
								shapes[i].setzPos(Float.parseFloat(coordinateElement.getAttribute("z")));

							for (int d = 0; d < coordinateElement.getChildNodes().getLength(); d++) {
								Node materialSolid = coordinateElement.getChildNodes().item(d);
								if (materialSolid.getNodeType() == Node.ELEMENT_NODE) {
									Element materialSolidElement = (Element) materialSolid;
									if (materialSolidElement.getNodeName().equals("color")) {
										shapes[i].setColorrgb(
												Float.parseFloat(materialSolidElement.getAttribute("r")),
												Float.parseFloat(materialSolidElement.getAttribute("g")),
												Float.parseFloat(materialSolidElement.getAttribute("b")));

									}
									if (materialSolidElement.getNodeName().equals("phong")) {
										if (materialSolidElement.getAttribute("ka").length() != 0)
											shapes[i]
													.setKa(Float.parseFloat(materialSolidElement.getAttribute("ka")));
										if (materialSolidElement.getAttribute("ks").length() != 0)
											shapes[i]
													.setKs(Float.parseFloat(materialSolidElement.getAttribute("ks")));
										if (materialSolidElement.getAttribute("kd").length() != 0) {
											shapes[i]
													.setKd(Float.parseFloat(materialSolidElement.getAttribute("kd")));
										}
										if (materialSolidElement.getAttribute("exponent").length() != 0) {
											shapes[i]
													.setExponent(Float.parseFloat(materialSolidElement.getAttribute("exponent")));
										
										}
									}

								}
							}
						}
					}

				}

			}
				
			int width = resolutionW;
			int height = resolutionH;
			BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		
			/*
			 * 
			 * from Tutorial slides page 8
			 * Line: 163-166
			 */
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					float x_n = (x + 0.5f) / width;
					float y_n = (height - y + 0.5f) / height;
					float x_i = (2 * x_n) - 1;
					float y_i = (2 * y_n) - 1;
					boolean pixelisSet = false; // if i already set the pixel to objects color i dont wanna overwrite it
					boolean shadowed = false;							
					// check intersection if intersects than do white
					for (int i = 0; i < shapes.length; i++) { // rayOrigin
						float inters = shapes[i].checkIntersect(cameraPos,
								new Vector3(x_i, y_i, -1.0f).normalize(),
								new Vector3(shapes[i].getxPos(), shapes[i].getyPos(), shapes[i].getzPos()),
								shapes[i].getRadius()); // calculate if intersects with sphere

						if (inters != -1.0f) { // if intersects with object
							for(int k = 0; k < shapes.length;k++) {
							
							// check if ray from point of intersection with sphere shot in light dir is hitting some other object	
							if(i > k && shapes[k].checkIntersect(cameraPos.plus(new Vector3(x_i, y_i, -1.0f).normalize().timesScalarfloat(inters)),new Vector3(-lightPos.x,-lightPos.y,-lightPos.z).normalize(),shapes[k].getxyz(), shapes[k].getRadius() ) != -1) {
							
								Vector3 colorShade = new Vector3(shapes[i].getRGB().getRed(),shapes[i].getRGB().getGreen(),shapes[i].getRGB().getBlue());
								colorShade = colorShade.timesScalarfloat(shapes[k].ka);
								
								if(colorShade.x > 255) colorShade.x= 255;
								if(colorShade.y > 255) colorShade.y= 255;
								if(colorShade.z > 255) colorShade.z= 255;
								
								image.setRGB(x, y, new Color(colorShade.x/255,colorShade.y/255, colorShade.z/255).getRGB());
								shadowed = true;
								pixelisSet = true;
							}
							}
							
							
							
						 if(!shadowed) {
							Vector3 Normal = shapes[i].getNormal(cameraPos,
									new Vector3(shapes[i].getxPos(), shapes[i].getyPos(), shapes[i].getzPos()), inters,
									new Vector3(x_i, y_i, -1.0f).normalize()); // get the normal of that pixel
							Vector3 phongdiff = shapes[i].shadePhong(cameraPos, lightPos, new Vector3(x_i, y_i, -1.0f),
									Normal.normalize(), new Vector3(shapes[i].getRGB().getRed(),
											shapes[i].getRGB().getGreen(), shapes[i].getRGB().getBlue())); // calculate
																											// color of
																											// pixel

							if (phongdiff.x / 255 > 1 && phongdiff.y / 255 > 1 && phongdiff.z / 255 > 1) { // checking
																											// if there
																											// is some
																											// values
																											// that are
																											// outside
																											// the range
								image.setRGB(x, y, new Color(1.0f, 1.0f, 1.0f).getRGB());
								break;
							} else if (phongdiff.x / 255 > 1 && phongdiff.y / 255 > 1) {
								image.setRGB(x, y, new Color(1.0f, 1.0f, phongdiff.z / 255).getRGB());
								break;
							} else if (phongdiff.x / 255 > 1 && phongdiff.z / 255 > 1) {
								image.setRGB(x, y, new Color(1.0f, phongdiff.y / 255, 1.0f).getRGB());
								break;
							} else if (phongdiff.y / 255 > 1 && phongdiff.z / 255 > 1) {
								image.setRGB(x, y, new Color(phongdiff.x / 255, 1.0f, 1.0f).getRGB());
								break;
							} else if (phongdiff.x / 255 > 1) {
								image.setRGB(x, y, new Color(1.0f, phongdiff.y / 255, phongdiff.z / 255).getRGB());
								break;
							} else if (phongdiff.y / 255 > 1) {
								image.setRGB(x, y, new Color(phongdiff.x / 255, 1.0f, phongdiff.z / 255).getRGB());
								break;
							} else if (phongdiff.z / 255 > 1) {
								image.setRGB(x, y, new Color(phongdiff.x / 255, phongdiff.y / 255, 1.0f).getRGB());

							} else
								image.setRGB(x, y,
										new Color(phongdiff.x / 255, phongdiff.y / 255, phongdiff.z / 255).getRGB());
							pixelisSet = true;
						}
						}
						else {
							if (!pixelisSet) 	image.setRGB(x, y, new Color(backGcolor.x/255, backGcolor.y/255, backGcolor.z/255).getRGB());
						}

					}

				}
			}
			
			File file2 = new File(FileOutputName);
			ImageIO.write(image, "png", file2);	
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
