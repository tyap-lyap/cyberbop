package tyaplyap.cyberbop.extension;

import tyaplyap.cyberbop.block.entity.IEnergy;

public interface PlayerExtension extends IEnergy {

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

	String getModule1();
	String getModule2();
	String getModule3();

	void setModule1(String module);
	void setModule2(String module);
	void setModule3(String module);

	boolean containsModule(String module);
}
