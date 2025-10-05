package tyaplyap.cyberbop.extension;

public interface PlayerExtension {

	boolean isCyborg();
	void setCyborg(boolean isCyborg);

	void setCyborgHead(String head);
	void setCyborgBody(String body);
	void setCyborgRightArm(String rightArm);
	void setCyborgLeftArm(String leftArm);
	void setCyborgRightLeg(String rightLeg);
	void setCyborgLeftLeg(String leftLeg);

	String getCyborgHead();
	String getCyborgBody();
	String getCyborgRightArm();
	String getCyborgLeftArm();
	String getCyborgRightLeg();
	String getCyborgLeftLeg();

	int getCyborgEnergy();
	int getCyborgMaxEnergy();
	void setCyborgEnergy(int cyborgEnergy);
}
