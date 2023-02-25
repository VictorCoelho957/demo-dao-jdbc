package application;

import java.util.List;
//import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
	DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

		//System.out.println("=== TEST 1: findById =======");
		///Department dep = departmentDao.findById(1);
	  // System.out.println(dep);
		
		System.out.println("\n=== TEST 2: findAll =======");
		List<Department> list = departmentDao.findAll();
		for (Department d : list) {
		System.out.println(d);
		}

		/*System.out.println("\n=== TEST 3: insert =======");
		
		System.out.println("insira o nome do novo departamento: ");
		String name=sc.next();
		Department newDepartment = new Department(null, name);
		departmentDao.insert(newDepartment);
		System.out.println("Departarmanto inserido! id: " + newDepartment.getId()); */


		
	
		/*Department newDepartment = new Department(null, "Music");
		departmentDao.insert(newDepartment);
		System.out.println("Inserted! New id: " + newDepartment.getId()); */

		/*System.out.println("\n=== TEST 4: update =======");
		System.out.println("Insira o id do usario que deseja atualizar: ");
		Integer id=sc.nextInt();
		Department dep2 = departmentDao.findById(id);
		System.out.println("Agora insira o novo nome: ");
		String name1=sc.next(); 
		
		dep2.setName(name1);
		departmentDao.update(dep2);
		System.out.println("Update completed"); */
		
		System.out.println("\n=== TEST 5: delete =======");
		System.out.print("Insira o dado que deseja excluir: ");
		int id1 = sc.nextInt();
		departmentDao.deleteById(id1);
		System.out.println("Delete completed"); 

		sc.close();
	}
}
