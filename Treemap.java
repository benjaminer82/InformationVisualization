import java.io.*;
import java.lang.*;
import java.util.*;

public class Treemap {
    public static FileFilter fileFilter = new FileFilter() {
             public boolean accept(File file) {
                 return file.isDirectory();
             } 
         }; 
    public static Hashtable record = new Hashtable();
    public static int l = 0, m = 0, sumSubDir = 0, deep = 0, n = 0;
    public static int maxl = 0;
    public static String pathIndex[] = new String[5000];
    public static double X = 100.0,Y = 2.0,Z = 100.0;
    
    public static void visitAllDirs(File dir, int l) {
        double transX = getTransX(dir);
        double transY = getTransY(dir);
        double transZ = getTransZ(dir);
        double parX = getX(dir);
        double parY = getY(dir);
        double parZ = getZ(dir);
        double transP = getTransP(dir);
        double height = getL(dir);
        double currX[] = new double[1000];
        int[] results = new int[1000];
        double currY = 0;
        double currZ = 0;
        double sum = 0.0;
        try{  
             if (dir.isDirectory()) {
                 File[] children = dir.listFiles(fileFilter);                 
                 if (calculate(dir) != 0)
                 {
                 for (int a = 0; a < children.length; a++){ 
		     sumSubDir = 0;
                     results[a] = calculate(children[a]);
                     sumSubDir = 0;
                     if (results[a] == 0){
                         results[a] = 1;
                     }
                     sum += results[a];
                 }
                 if(l > maxl) {maxl = l;}
                 for (int b = 0; b < children.length; b++){
                     if(b == 0){
                         currX[b] = transX + parX/2 - ((results[b]/sum)*parX/2);
                         currY = (height + 1) * Y;
                         currZ = transZ;
                         transP = results[b] * 1.0 / sum;
                         record.put(children[b].getPath(), children[b].getName()+ "," + l + "," + currX[b] + "," + currY + "," + currZ + "," + (results[b]/sum)*parX + "," + Y + "," + Z +"," + transP);
                     }
                     else{
                         currX[b] = currX[b-1] - (results[b - 1]/sum)*parX/2 - (results[b]/sum)*parX/2;
                         currY = (height + 1) * Y;
                         currZ = transZ; 
                         record.put(children[b].getPath(), children[b].getName()+ "," + l + "," + currX[b] + "," + currY + "," + currZ + "," + (results[b]/sum)*parX + "," + Y + "," + Z +"," + transP);  						          
                     }
                     pathIndex[n] = children[b].getPath();
                     n++;
                 }
                 }
                 for (int i = 0; i < children.length; i++) {     
                     visitAllDirs(new File(dir, children[i].getName()), l + 1);
                 } 
            }
        }catch(Exception e){
		    String err=e.toString();
		     System.out.println("problem:"+err);
		}
    }
    
    public static int calculate(File pathName){
         try{
            if (pathName.isDirectory()){                
                File[] children = pathName.listFiles(fileFilter);
                sumSubDir += children.length;
                for(int i = 0; i < children.length; i++){
                    calculate(new File(pathName, children[i].getName()));
                }
             } 
         }catch(Exception e){
		    String err=e.toString();
		     System.out.println("pro2 "+err);
		 } 
         return sumSubDir;
    }
    
    public static double getTransX(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double transX = Double.parseDouble(w[2]);
        return transX;
    }
    
    public static double getTransY(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double transY = Double.parseDouble(w[3]);
        return transY;
    }
    
    public static double getTransZ(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double transZ = Double.parseDouble(w[4]);
        return transZ;
    }
    
    public static double getX(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double X = Double.parseDouble(w[5]);
        return X;
    }
    
    public static double getY(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double Y = Double.parseDouble(w[6]);
        return Y;
    }
    
    public static double getZ(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double Z = Double.parseDouble(w[7]);
        return Z;
    }
    
    public static double getTransP(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double transP = Double.parseDouble(w[8]);
        return transP;
    }
    
    public static double getL(File pathName){
        String v = (String) record.get(pathName.getPath());
        String[] w = v.split(",");
        double l = Double.parseDouble(w[1]);
        return l;
    }    
    public static void VRMLDrawer(double transX, double transY, double transZ, double sizeX, double sizeY, double sizeZ, String color, double transP, String name, String path){
        try{
        FileWriter f = new FileWriter("3DTreemap.wrl",true);
        f.write("Transform {\ntranslation	" + transX + " " + transY + " " + transZ + "\n\nchildren [\n		Anchor {\n		url	\""+ path +"\"\n\nchildren [\n	Shape {\n\n		appearance Appearance {\n\n			material Material {\n\n				diffuseColor " + color + "\n				transparency " + transP + " \n\n\n			}\n		}\n\n\n		geometry Box {\n\n\n			size	" + sizeX + " "  + sizeY + " " + sizeZ + "\n		}\n	}\n	Shape {\n		appearance Appearance {\n			material Material {\n				diffuseColor 1 1 1\n			}\n		}\n		geometry Text {\n			string \"" + name + "\"\n\n			fontStyle FontStyle	{\n				size 2\n				style \"BOLD\"\n			}\n\n		}\n	}\n]\n	}\n]\n}\n");
        f.close();
        }
        catch(Exception e){}
    }
    
    public static void main(String[] args) {
        try{
            File dir = new File("d:\\a");            
            File[] children = dir.listFiles(fileFilter);    
            double transX = 0.0, transY = 0.0, transZ = 0.0;
            double transP = 0.2;
             
            record.put(dir.getPath(), dir.getName()+ "," + l + "," + transX + "," + transY + "," + transZ + "," + X + "," + Y + "," + Z +"," + transP);         

            visitAllDirs(dir,0);

            try{
                FileWriter f = new FileWriter("3DTreemap.wrl",true);
                f.write("#VRML V2.0 utf8\n");
                f.close();
            }catch(Exception e){}
            
            try{
                String info1 = (String) record.get(dir.getPath());
                String[] information1 = info1.split(",");                
                String color1 = "0 0.5 1";
                String name1 = information1[0];            
                VRMLDrawer(transX, transY, transZ, X, Y, Z, color1, transP, name1, dir.getPath());
             }catch(Exception e){}
            
            for ( int d = 0; d < n; d++){
                String info = (String) record.get(pathIndex[d]);
                String[] information = info.split(",");
                String name = information[0];
                double l1 = Double.parseDouble(information[1]);
                double transX1 = Double.parseDouble(information[2]);
                double transY1 = Double.parseDouble(information[3]);
                double transZ1 = Double.parseDouble(information[4]);
                double sizeX = Double.parseDouble(information[5]);
                double sizeY = Double.parseDouble(information[6]);
                double sizeZ = Double.parseDouble(information[7]);
                double transP1 = Double.parseDouble(information[8]);
                double transPf = 0.6 / maxl * l1 + 0.1 + 0.1 * transP1;
                String color = (1.0 / maxl * l1 + 0) + " 0.5 " + (1 - 1.0 / maxl * l1);                
                VRMLDrawer(transX1, transY1, transZ1, sizeX, sizeY, sizeZ, color, transPf, name, pathIndex[d]);
            }
            
            try{
                FileWriter f = new FileWriter("3DTreemap.wrl",true);
                f.write("\n\nViewpoint {\n    position    0 55.0 90.0\n\n    orientation 1.0 0.0 0.0 -0.7\n    fieldOfView 1\n    description \"Entire View\"\n\n}\n");
                f.close();
            }catch(Exception e){}
            
        }catch(Exception e){}
    }
    
}
