import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PackSimulator {

    private Random rand = new Random();
    private int rarity;
    private String returnThis = "";
    private String rare = "";
    private String foil = "";
    private boolean getFoil = false;
    private boolean getSpecial = false;
    private String special = "";
    private String uncommon1 = "";
    private String uncommon2 = "";
    private String uncommon3 = "";
    private boolean packNotFound = false;

    PackSimulator() {

    }

    void openPack(String set) throws FileNotFoundException {
        int card;
        int i;
        if(set.equals("chaos ")){
            Scanner chaosScan = new Scanner(new File("Chaos"));
            String[] chaos = new String[8];
            i = 0;
            while(chaosScan.hasNext()){
                chaos[i] = chaosScan.nextLine();
                i++;
            }
            card = rand.nextInt(8);
            set = chaos[card] + " ";
            returnThis = returnThis + "Your chaos pack is " + chaos[card] + ".";
        }
        switch (set) {
            default:
                packNotFound = true;
                break;
            case "dominaria ":

                //creates the array of cards
                String cards[] = createSet(258, "Dominaria");
                //generates the pack of cards
                generateCards(cards, 15, 56, 82, 104);

                break;
            case "hour of devastation ":

                cards = createSet(218, "Hour of Devastation");
                generateCards(cards, 14, 44, 62, 74);
                //invocation check
                masterpieceCheck(cards, 32);

                break;
            case "aether revolt ":

                cards = createSet(213, "Hour of Devastation");
                generateCards(cards, 10, 44, 61, 74);
                //invocation check
                masterpieceCheck(cards, 24);

                break;
            case "kaladesh ":

                cards = createSet(287, "Kaladesh");
                generateCards(cards, 15, 55, 82, 105);
                //invocation check
                masterpieceCheck(cards, 30);

                break;
            case "amonkhet ":

                cards = createSet(295, "Amonkhet");
                generateCards(cards, 15, 55, 82, 113);
                masterpieceCheck(cards, 27);

                break;
            case "war of the spark ":

                cards = createSet(251, "War of the Spark");
                generateCards(cards, 15, 53, 80, 103);

                break;
            case "guilds of ravnica ":

                cards = createSet(252, "Guilds of Ravnica");
                generateCards(cards, 15, 53, 80, 104);

                break;
            case "m20 ":

                cards = createSet(312, "M20");
                generateCards(cards, 15, 58, 86, 152);

                break;
            case "ravnica allegiance ":

                cards = createSet(260, "Ravnica Allegiance");
                generateCards(cards, 15, 53, 82, 108);

                break;
            case "tempest ":

                cards = createSet(330, "Tempest");

                card = rand.nextInt(110);
                rare = cards[card];
                card = rand.nextInt(110)+110;
                uncommon1 = cards[card];
                card = rand.nextInt(110)+110;
                uncommon2 = cards[card];
                card = rand.nextInt(110)+110;
                uncommon3 = cards[card];
                break;
        }
    }

    private void masterpieceCheck(String[] cards, int numMasterpieces) {

        int num;
        num = rand.nextInt(144);
        if(num == 69){
            getSpecial = true;
            num = rand.nextInt(numMasterpieces)+cards.length - numMasterpieces;
            special = cards[num];
        }
    }

    private String getFoilLand(){
        int land = rand.nextInt(5);
        String returnThis;
        switch(land){
            case 0: returnThis = "Island";
                break;
            case 1: returnThis = "Swamp";
                break;
            case 2: returnThis = "Plains";
                break;
            case 3: returnThis = "Mountain";
                break;
            case 4: returnThis = "Forest";
            break;
            default: returnThis = "";
        }
        return returnThis;
    }

    private void generateCards(String[] cards, int mythics, int rares, int uncommons, int commons){
        int num;
        int foilCheck;

        rarity = rand.nextInt(8) + 1;
        if (rarity == 8) {
            num = rand.nextInt(mythics);
            rare = cards[num];
        }
        num = rand.nextInt(rares)+mythics;
        rare = cards[num];
        num = rand.nextInt(uncommons)+mythics+rares;
        uncommon1 = cards[num];
        num = rand.nextInt(uncommons)+mythics+rares;
        uncommon2 = cards[num];
        num = rand.nextInt(uncommons)+mythics+rares;
        uncommon3 = cards[num];
        //foil check
        foilCheck = rand.nextInt(63);
        if (foilCheck < 15) {
            getFoil = true;
            num = rand.nextInt(128);
            if (num < 88) {
                num = rand.nextInt(commons)+uncommons+mythics+rares;
                foil = cards[num];
            } else if(num < 112){
                num = rand.nextInt(uncommons)+mythics+rares;
                foil = cards[num];
            } else if(num < 119){
                num = rand.nextInt(rares)+mythics;
                foil = cards[num];
            } else if(num == 119){
                num = rand.nextInt(mythics);
                foil = cards[num];
            } else {
                foil = getFoilLand();
            }
        }
    }

    private String[] createSet(int cardAmount, String setName) throws FileNotFoundException {
        int i = 0;
        String[] cards = new String[cardAmount];
        Scanner scan = new Scanner(new File(setName));
        while (scan.hasNext()) {
            cards[i] = scan.nextLine();
            i++;
        }
        return cards;
    }

    public String toString() {
        returnThis = returnThis + " Your pack contains: - Uncommons: " + uncommon1 + "; " + uncommon2 + "; " + uncommon3 + " - Rare: " + rare + ". ";
        if(getFoil){
            returnThis = returnThis + "You also got a foil: " + foil + "! ";
        }

        if(getSpecial){
            returnThis = returnThis + "You also got a MASTERPIECE: " + special + "! ";
        }

        if(packNotFound){
            return "pack doesnt exist/hasnt been implmented (yet)";
        }
        return returnThis;
    }
}
