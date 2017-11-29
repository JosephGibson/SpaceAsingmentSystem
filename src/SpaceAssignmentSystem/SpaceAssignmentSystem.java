package SpaceAssignmentSystem;



public class SpaceAssignmentSystem  {
	public static void main(String[] args) {
		// scheduler the rendering event.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {	
			public void run() {
				try {			
					Scheduler s = new Scheduler();
					RequestHandler RH = new RequestHandler(s);
					GuiBuilder.renderGUI(RH);
				} catch (SchedulerException e) {
					// TODO Auto-generated catch block
					System.out.println("Whoops bug splat");
					e.printStackTrace();
				}
			}
		});
	}
}
