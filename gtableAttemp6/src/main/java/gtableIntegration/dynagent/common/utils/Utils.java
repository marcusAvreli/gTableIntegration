package gtableIntegration.dynagent.common.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Common/src/dynagent/common/utils/Utils.java
public class Utils {
	   public static Dimension getDimString(String value, boolean bold, Font font, FontRenderContext fontRender, float factorMultiplySize){
	    	if (value == null)
	        {
	            return new Dimension(0, 0);
	        }
	        //getStringBounds no es una funcion que garantice totalmente que la cadena ocupa exactamente ese tamaño,
	        //es mas bien una estimación. Para evitar errores podriamos hacer value+" " pq hay casos en el que no se muestra
	        //la cadena completa usando el espacio calculado. Además de esta manera evitariamos que un label quede demasiado
	        //cerca del componente. Sin embargo hay casos en el que queda demasiado alejado. De momento multiplicamos el ancho
	        //por un valor que se cumpla en la fuente que estemos usando, para la fuente actual Comic Sans MS 1,06 es el valor que utilizamos.
	        if (bold)
	        {
	            Font fontBold= font.deriveFont(Font.BOLD,(font.getSize2D()*factorMultiplySize));
	            Rectangle2D rect = fontBold.getStringBounds(value, fontRender);
	            Dimension size=rect.getBounds().getSize();
	            size.width=(int)(size.width/**1.06*/);
	            return size;
	        }
	        else
	        {
	        	Font fontAux=font.deriveFont((font.getSize2D()*factorMultiplySize));
	            Rectangle2D rect = fontAux.getStringBounds(value, fontRender);
	            Dimension size=rect.getBounds().getSize();
	            size.width=(int)(size.width/**1.06*/);
	            return size;
	        }
	    }
	   
	 //PARA QUE RESPETE LAS PROPERTIES QUE SON ACRONIMOS
		public static String normalizeLabel(String label){
			if(label!=null){
				//Si el label tiene alguna letra que esta en minusculas ponemos todas en minusculas para luego ir poniendo en mayusculas solo la primera y la que sigue a un caracter especial
				//Si todo el label esta en mayusculas se respeta y lo unico que se hace es sustituir los '_' por ' ' (Esto tambien se sustituye para las minusculas)
				boolean foundLowerCase=false;
				if(label.length()<4 || (label.contains("-") && !label.contains("_") && !label.contains(" "))){//Solo buscamos minusculas si el label es menor que 4(Supuestos acronimos) o tiene algun guion(esto lo hacemos por NIF-CIF-VAT) salvo que tenga espacios, en ese caso no seria un acrónimo
					for(int i=0;i<label.length() && !foundLowerCase;i++){
						foundLowerCase=Character.isLetter(label.charAt(i)) && Character.isLowerCase(label.charAt(i));
					}
				}else foundLowerCase=true;
				StringBuffer buffer=new StringBuffer(foundLowerCase?label.toLowerCase():label);
				int index;
				while((index=buffer.indexOf("_"))!=-1){
					buffer.replace(index, index+1, " ");
				}
				if(foundLowerCase){//Realizamos la transformacion poniendo en mayusculas la primera letra de cada palabra o la que sigue a un caracter especial
					buffer.replace(0, 1, (String.valueOf(buffer.charAt(0))).toUpperCase());
					String[] listSearch={" ","(","<","%","'","-"};//TODO Seria mejor hacer que siempre se tenga en cuenta cuando el caracter no es alfanumerico
					int size=listSearch.length;
					for(int i=0;i<size;i++){
						String subString=listSearch[i];
						int indexNextChar=0;
						while((index=buffer.indexOf(subString,indexNextChar))!=-1){
							indexNextChar=index+1;
							if(buffer.length()>indexNextChar){
								int indexProx=buffer.indexOf(subString,indexNextChar);
								if((indexProx==-1 && indexNextChar+2<=label.length()-1) || (indexProx!=-1 && indexProx>indexNextChar+2))
									buffer.replace(indexNextChar, indexNextChar+1, (String.valueOf(buffer.charAt(indexNextChar))).toUpperCase());
							}else break;
						}
					}
				}
				return buffer.toString().trim();
			}else return null;
		}
		
		public static String normalizeWindowTitle(String label,String labelObjeto){
			return normalizeLabel(label)+(labelObjeto!=null?" '"+/*normalizeLabel(*/labelObjeto/*)*/+"'":"");
			//return normalizeLabel(label)+(labelObjeto!=null?" < "+normalizeLabel(labelObjeto)+" >":"");
		}
}
