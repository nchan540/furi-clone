package constants;

public class Settings {

    //boss info
    public boolean[] bossesAllowed;
    public int maxBosses;
    public int bossesToKill;
    
    //game info
    public boolean arcade;




    public Settings() {
        arcadePreset();
    }

    public void arcadePreset() {
        bossesAllowed = new boolean[]{true, true, true, false, false};
        maxBosses = 2;

    }

    public void arcadePreset(int maxBosses, boolean pain) {
        bossesAllowed = new boolean[]{true, true, true, pain, pain};
        this.maxBosses = maxBosses;
        bossesToKill = 3;
        arcade = true;
    }

    public void campaignPreset(int level) {
        switch (level) {
            case 0:
                bossesAllowed = new boolean[]{true, false, false, false, false};
                this.maxBosses = 1;
                bossesToKill = 1;
                break;
            case 1:
                bossesAllowed = new boolean[]{false, true, false, false, false};
                this.maxBosses = 1;
                bossesToKill = 1;
                break;
            case 2:
                bossesAllowed = new boolean[]{false, false, true, false, false};
                this.maxBosses = 1;
                bossesToKill = 1;
                break;
            case 3:
                bossesAllowed = new boolean[]{true, true, false, false, false};
                this.maxBosses = 2;
                bossesToKill = 2;
                break;
            case 4:
                bossesAllowed = new boolean[]{false, true, true, false, false};
                this.maxBosses = 2;
                bossesToKill = 2;
                break;
            case 5:
                bossesAllowed = new boolean[]{false, false, false, true, false};
                this.maxBosses = 1;
                bossesToKill = 1;
                break;
            case 6:
                bossesAllowed = new boolean[]{false, false, false, false, true};
                this.maxBosses = 1;
                bossesToKill = 1;
                break;
        }
        arcade = false;
    }
}