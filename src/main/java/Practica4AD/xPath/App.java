package Practica4AD.xPath;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import javax.xml.transform.OutputKeys;

/**
 * Hello world!
 *
 */
public class App {
	private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
	private static String USER = "admin";
	private static String PASSWORD = "admin";
	private static XPathQueryService xpath;
	/**
	 * args[0] Should be the name of the collection to access args[1] Should be the
	 * name of the resource to read from the collection
	 */
	final static String driver = "org.exist.xmldb.DatabaseImpl";
	Scanner reader = new Scanner(System.in);

	@SuppressWarnings("rawtypes")
	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
		// initialize database driver
		Class cl = null;
		int opcion = 0;
		try {
			cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);

			Collection collection = null;
			XMLResource res = null;

			// get the collection
			collection = (Collection) DatabaseManager.getCollection(URI, USER, PASSWORD);
			((Configurable) collection).setProperty(OutputKeys.INDENT, "no");
			xpath = Objects.requireNonNull(collection.getService(XPathQueryService.SERVICE_NAME, null));

			System.out.println("Productos:\n" + "1-Obtén los nodos denominación y precio de todos los productos.\n"
					+ "2-Obtén los nodos de los productos que sean placas base.\n"
					+ "3-Obtén los nodos de los productos con precio mayor de 60€ y de la zona 20.\n"
					+ "4-Obtén el número de productos que sean memorias y de la zona 10.\n"
					+ "5-Obtén la media de precio de los micros.\n"
					+ "6-Obtén los datos de los productos cuyo stock mínimo sea mayor que su stock actual.\n"
					+ "7-Obtén el nombre del producto y el precio de aquellos cuyo stock mínimo sea mayor\n"
					+ "que su stock actual y sean de la zona 40.\n" 
					+ "8-Obtén el producto más caro.\n" + "9-Obtén el producto más barato de la zona 20.\n"
					+ "10-Obtén el producto más caro de la zona 10.");

			opcion = Leer.pedirEnteroValidar();
			while (opcion != 0) {

				switch (opcion) {
				case 1:

					ResourceIterator Iterator = xpath.query("/productos/produc/*[self::denominacion or self::precio]").getIterator();
					while (Iterator.hasMoreResources()) {

						System.out.println();

						XMLResource res1 = ((XMLResource) Iterator.nextResource());
						System.out.println(res.getContent());

						if (Iterator.hasMoreResources()) {
							XMLResource precio = ((XMLResource) Iterator.nextResource());
							System.err.println(precio.getContent());
						}
					}

					break;
				case 2:
					ResourceIterator Iterator2 = xpath.query("/productos/produc[denominacion[contains(., 'Placa Base')]]").getIterator();

					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res2 = ((XMLResource) Iterator.nextResource());
						System.out.println(res.getContent());
					}

					break;

				case 3:
					ResourceIterator Iterator3 = xpath.query("/productos/produc[precio[text() > 60] and cod_zona[text() = 20]]").getIterator();

					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res3 = ((XMLResource) Iterator.nextResource());
						System.out.println(res.getContent());
					}

					break;
				case 4:
					ResourceIterator Iterator4 = xpath.query("count(/productos/produc[denominacion[contains(., 'Memoria')] and cod_zona[text() = 10]])").getIterator();

					System.out.println();
					XMLResource res4 = ((XMLResource) Iterator.nextResource());
					System.out.println("Hay " + res.getContent() + " productos «Memoria» de la zona 10.");

					break;
				case 5:
					ResourceIterator Iterator5 = xpath.query("sum(/productos/produc[denominacion[contains(., 'Micro')]]/precio/text()) div count(/productos/produc[denominacion[contains(., 'Micro')]])").getIterator();

					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res5 = ((XMLResource) Iterator.nextResource());
						System.out.println("La media de los precios de los microprocesadores es " + res.getContent());
					}

					break;
				case 6:
					ResourceIterator Iterator6 = xpath.query("/productos/produc[number(stock_minimo) > number(stock_actual)]").getIterator();

					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res6 = ((XMLResource) Iterator.nextResource());

						NodeList nodosProduc = res.getContentAsDOM().getChildNodes();

						for (int i = 0; i < nodosProduc.getLength(); ++i) {
							Node n = nodosProduc.item(i);
							if (n.getLocalName() != null) {
								System.out.println(n.getLocalName() + " = " + n.getTextContent());
							}
						}
					}

					break;
				case 7:

					ResourceIterator Iterator7 = xpath.query("/productos/produc/*[(self::denominacion or self::precio) and number(../stock_minimo/text()) > number(../stock_actual/text()) and ../cod_zona/text() = 40]/text()").getIterator();

					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res7 = ((XMLResource) Iterator.nextResource());
						System.out.println("nombre = " + res.getContent());

						if (Iterator.hasMoreResources()) {
							XMLResource res2 = ((XMLResource) Iterator.nextResource());
							System.out.println("precio = " + res2.getContent());
						}
					}

					break;
				case 8:

					ResourceIterator Iterator8 = xpath.query("/productos/produc[precio = max(/productos/produc/precio)]").getIterator();
					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res8 = ((XMLResource) Iterator.nextResource());
						System.out.println(res.getContent());
					}

					break;
				case 9:

					ResourceIterator Iterator9 = xpath.query("/productos/produc[precio = min(/productos/produc[cod_zona = 20]/precio)]").getIterator();
					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res9 = ((XMLResource) Iterator.nextResource());
						System.out.println(res.getContent());
					}

					break;
				case 10:

					ResourceIterator Iterator10 = xpath.query("/productos/produc[precio = max(/productos/produc[cod_zona = 10]/precio)]").getIterator();
					while (Iterator.hasMoreResources()) {
						System.out.println();
						XMLResource res10 = ((XMLResource) Iterator.nextResource());
						System.out.println(res.getContent());
					}

					break;

				default:
					System.out.println("Opcion no valida");
				}

				System.out.println("Productos:\n" + "1-Obtén los nodos denominación y precio de todos los productos.\n"
						+ "2-Obtén los nodos de los productos que sean placas base.\n"
						+ "3-Obtén los nodos de los productos con precio mayor de 60€ y de la zona 20.\n"
						+ "4-Obtén el número de productos que sean memorias y de la zona 10.\n"
						+ "5-Obtén la media de precio de los micros.\n"
						+ "6-Obtén los datos de los productos cuyo stock mínimo sea mayor que su stock actual.\n"
						+ "7-Obtén el nombre del producto y el precio de aquellos cuyo stock mínimo sea mayor\n"
						+ "que su stock actual y sean de la zona 40.\n" 
						+ "8-Obtén el producto más caro.\n" + "9-Obtén el producto más barato de la zona 20.\n"
						+ "10-Obtén el producto más caro de la zona 10.");

				opcion = Leer.pedirEnteroValidar();

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (XMLDBException e) {
			e.printStackTrace();
		}

	}

}