import model.BeepModel;

public class BeepMain {

	public static void main(String[] args) {

		BeepProperties p = new BeepProperties();
		try {
		BeepModel propConf = p.loadPropertiesToModel();
		BeepUtil.generateWavFromModel(propConf);
		}catch(Exception e) {
			
		}
	}

}
