import java.io.*;
import java.util.*;
import java.util.function.*;


import DB2.Relation;

public class DB4{

 class Relation{
	String name;
	HashSet<String> attributes = new HashSet<String>();
	HashSet<HashMap<String, String>> tuples = new HashSet<HashMap<String, String>>();
	boolean match = false;

  public Relation(){ }

  public Relation(String filename){
	Scanner in = null;
	try {
		in = new Scanner(new File(filename));
	} catch (FileNotFoundException e){
		System.err.println(filename + " not found.");
		System.exit(1);
	}
	String[] terms = in.nextLine().split("\t");
	name = terms[0];
	int cols = Integer.parseInt(terms[1]);
	int rows = Integer.parseInt(terms[2]);
	String[] att = new String[cols];
	terms = in.nextLine().split("\t");
	for (int c = 0; c < cols; c++){
		att[c] = terms[c];
		attributes.add(terms[c]);
	}
	for (int r = 0; r < rows; r++){
		HashMap<String, String> tuple = new HashMap<String, String>();
		terms = in.nextLine().split("\t");
		for (int c = 0; c < cols; c++) tuple.put(att[c], terms[c]);
		tuples.add(tuple);
	}
	in.close();
  }

  public void showRelation(){
	System.out.println(name + "\t" + attributes.size() + "\t" + tuples.size());
	attributes.forEach(s -> System.out.print(s + "\t"));
	System.out.println();
	tuples.forEach(t -> {
		attributes.forEach(s -> System.out.print(t.get(s) + "\t"));
		System.out.println();
	});
  }

  public Relation project(String... attrs){
	Relation PR = new Relation();
	int cols = attrs.length;
	PR.name = "project(" + name;
	for (int c = 0; c < cols; c++){
		if (!attributes.contains(attrs[c])){
			System.err.println("attribute " + attrs[c] + " not found.");
			System.exit(1);
		}
		PR.name += "," + attrs[c];
		PR.attributes.add(attrs[c]);
	}
	PR.name += ")";
	tuples.forEach(t -> {
		HashMap<String, String> tuple = new HashMap<String, String>();	
		PR.attributes.forEach(s -> tuple.put(s, t.get(s)));
		PR.tuples.add(tuple);
	});
	return PR;
  }

  public Relation select(Function<HashMap<String, String>, Boolean> condition){
	Relation SR = new Relation();
	SR.name = "select(" + name + ",condition)";
	SR.attributes.addAll(attributes);
	tuples.forEach(t -> { 
		if(condition.apply(t)) SR.tuples.add(t);
	});
	return SR;
  }


  public Relation naturalJoin(Relation other){
	Relation NJ = new Relation();
	NJ.name = name + " X " + other.name;
	NJ.attributes.addAll(attributes);
	NJ.attributes.addAll(other.attributes);
	HashSet<String> commonAttributes = new HashSet<String>(attributes);
	commonAttributes.retainAll(other.attributes);
	tuples.forEach(t -> 
		other.tuples.forEach(t2 -> {
			match = true;
			commonAttributes.forEach(c -> match = match && t.get(c).equals(t2.get(c)));
			if (match){
				HashMap<String, String> tuple = new HashMap<String, String>();
				t.forEach((k, v) -> tuple.put(k, v));
				t2.forEach((k, v) -> tuple.put(k, v));
				NJ.tuples.add(tuple);
			}
		}));
	return NJ;
  }

  public Relation thetaJoin(Relation other, BiFunction<HashMap<String, String>, HashMap<String, String>, Boolean> condition){
	HashSet<String> commonAttributes = new HashSet<String>(attributes);
	commonAttributes.retainAll(other.attributes);
	Relation TJ = new Relation();
	TJ.name = name + " TX " + other.name;
	commonAttributes.forEach(c -> {
		TJ.attributes.add(name + "." + c);
		TJ.attributes.add(other.name + "." + c);
	});
	attributes.forEach(c -> {
		if (!commonAttributes.contains(c)) TJ.attributes.add(c);
	});
	other.attributes.forEach(c -> {
		if (!commonAttributes.contains(c)) TJ.attributes.add(c);
	});
	tuples.forEach(t -> 
		other.tuples.forEach(t2 -> {
			if (condition.apply(t, t2)){
				HashMap<String, String> tuple = new HashMap<String, String>();
				t.forEach((k, v) -> {
					if (commonAttributes.contains(k)) tuple.put(name + "." + k, v);
					else tuple.put(k, v);
				});
				t2.forEach((k, v) -> {
					if (commonAttributes.contains(k)) tuple.put(other.name + "." + k, v);
					else tuple.put(k, v);
				});
				TJ.tuples.add(tuple);
			}
		}));
	return TJ;
  }

  public Relation intersection(Relation other){
	if (!attributes.equals(other.attributes)) return null;
	Relation R = new Relation();
	R.name = name + " n " + other.name;
	R.attributes.addAll(attributes);
	R.tuples.addAll(tuples);
	R.tuples.retainAll(other.tuples);
	return R;
 }

  public Relation union(Relation other){
	if (!attributes.equals(other.attributes)) return null;
	Relation R = new Relation();
	R.name = name + " U " + other.name;
	R.attributes.addAll(attributes);
	R.tuples.addAll(tuples);
	R.tuples.addAll(other.tuples);
	return R;
 }

  public Relation difference(Relation other){
	if (!attributes.equals(other.attributes)) return null;
	Relation R = new Relation();
	R.name = name + " - " + other.name;
	R.attributes.addAll(attributes);
	R.tuples.addAll(tuples);
	R.tuples.removeAll(other.tuples);
	return R;
 }
	
 };

  void queryAnswering(){
	Relation Product = new Relation("Product.txt");
	Relation PC = new Relation("PC.txt");
	Relation Laptop = new Relation("Laptop.txt");
	Relation Printer = new Relation("Printer.txt");

	//a
	Relation R2 = PC.select(r -> Double.parseDouble(r.get("speed")) >= 3.00).project("model");
 	R2.showRelation();
 	
 	
 	System.out.println("\n");
 	//b
 	Relation R1 = Laptop.select(j -> Integer.parseInt(j.get("hd")) >= 100).project("model");
 	R1.thetaJoin(Product, (x, y) -> x.get( "model" ).equals(y.get( "model" ))).project("maker").showRelation();
 	
 	
 	System.out.println("\n");
 	//c
 	Relation R5 = Product.select(j -> (j.get("maker").equals("B"))).project("model");
 	Relation R6 = (R5.naturalJoin(PC)).project("model","price");
 	Relation R7 = R5.naturalJoin(Printer).project("model","price");
 	Relation R8 = R5.naturalJoin(Laptop).project("model","price");
 	Relation R9 = R6.union(R7).union(R8);
 	R9.showRelation();
 	
 	
 	System.out.println("\n");
 	//d
 	Relation R10 = Printer.select(j -> (j.get("color").equals("true")) && j.get("type").equals("laser")).project("model");
 	R10.showRelation();
 	
 	
 	System.out.println("\n");
 	//e
 	Relation R11 = Product.select(j-> j.get("type").equals("laptop")).project("maker");
 	Relation R12 = Product.select(j-> j.get("type").equals("pc")).project("maker");
 	R11.difference(R12).project("maker").showRelation();
 	
 	
 	System.out.println("\n");
 	//f
 	Relation R13 = PC.thetaJoin(PC, (x, y) -> x.get("hd").equals(y.get("hd")) && !x.get("model").equals(y.get("model"))).project(PC.name + ".model");
 	R13.showRelation();
 	
 	
 	System.out.println("\n");
 	//g
 	Relation R14 = PC.thetaJoin(PC, (x, y) -> x.get("speed").equals(y.get("speed")) && x.get("ram").equals(y.get("ram")) && !x.get("model").equals(y.get("model"))).project(PC.name + ".model");
 	R14.showRelation();
 	
 	
 	System.out.println("\n");
 	//h
 	Relation R15 = PC.select(j-> Double.parseDouble(j.get("speed")) >= 2.80).project("model");
 	Relation R16 = Laptop.select(j-> Double.parseDouble(j.get("speed")) >= 2.80).project("model");
 	Relation R17 = R15.union(R16).project("model");
 	Relation R18 = R17.naturalJoin(Product).project("model","maker");
 	Relation R19 = R18.thetaJoin(R18, (x, y) ->x.get("maker").equals(y.get("maker")) && !x.get("model").equals(y.get("model"))).project(R18.name + ".maker");
 	R19.showRelation();
 	
 	System.out.println("\n");
 	//i
 	Relation R22 = PC.project("model","speed").union(Laptop.project("model","speed")).project("model","speed");
 	Relation R32 = R22.naturalJoin(Product).project("model","speed","maker");
 	Relation R33 = R32.thetaJoin(R32, (x,y) -> x.get("model").equals(y.get("model"))).project(R32.name + ".model",R32.name + ".speed",R32.name + ".maker");
 	Relation R23 = R32.thetaJoin(R32, (x,y) -> Double.parseDouble(x.get("speed")) > Double.parseDouble(y.get("speed")) ).project(R32.name + ".model",R32.name + ".speed",R32.name + ".maker");
 	Relation R24 = R33.difference(R23).project(R32.name + ".model", R32.name + ".speed",R32.name + ".maker");
 	Relation R25 = R32.naturalJoin(R24).project(R32.name + ".maker");
 	R25.showRelation();
 	

 	System.out.println("\n");
 	//j
 	Relation R50 = (Product.naturalJoin(PC)).project("speed","maker","model");
 	Relation R51 = (Product.naturalJoin(PC)).project("speed","maker","model");
 	Relation R52 = (Product.naturalJoin(PC)).project("speed","maker","model");
 	Relation R53 = R50.thetaJoin(R51, (x,y)-> x.get("maker").equals(y.get("maker")) && Double.parseDouble(x.get("speed")) != Double.parseDouble(y.get("speed")) ).project(R50.name + ".model",R51.name + ".model",R50.name + ".speed",R51.name + ".speed",R50.name + ".maker",R51.name + ".maker");
 	Relation R54 = R53.thetaJoin(R52, (x,y)-> x.get(R50.name + ".maker").equals(y.get("maker")) && Double.parseDouble(x.get(R50.name + ".speed")) != Double.parseDouble(y.get("speed"))  && Double.parseDouble(x.get(R51.name + ".speed")) != Double.parseDouble(y.get("speed")) ).project(R50.name + ".model",R50.name + ".speed",R50.name + ".maker");
 	Relation R55 = R54.project(R50.name + ".maker");
 	R55.showRelation();
 	
 	Relation R85 = PC.project("model");
 	Relation R86 = Product.naturalJoin(R85).project("maker","model");
 	Relation R87 = R86.project("maker","model");
 	Relation R88 = R86.project("maker","model"); 
 	Relation R89 = R86.thetaJoin(R87, (x, y) -> x.get("maker").equals(y.get("maker")) && !x.get("model").equals(y.get("model"))).project(R86.name + ".maker", R86.name +".model", R87.name +".maker", R87.name + ".model");      
 	Relation R90 = R89.thetaJoin(R88, (x, y) -> x.get(R86.name + ".maker").equals(y.get("maker")) && !x.get(R87.name +".model").equals(y.get("model"))
 	&& !x.get(R86.name +".model").equals(y.get("model"))).project(R88.name +".maker");
 	R90.showRelation();

    }

  public static void main(String[] args){
	DB4 db4 = new DB4();
	db4.queryAnswering();
  }
}