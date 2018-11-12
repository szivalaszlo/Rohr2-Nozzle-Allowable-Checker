import java.io.FileNotFoundException;

import org.junit.Test;

public class ApplicationTest {

	@Test
	public void testMain() throws FileNotFoundException {
		Application app = new Application();
		String[] args = new String[3];

		args[0] = "c:/nl/nozzle.out";
		args[1] = "c:/nl/allowables.ini";
		args[2] = "c:/nl/results.xls";
		
		app.main(args);
		
	}

}
