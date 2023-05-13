public class RandomNumber{
    public int lowerBound;
    public int upperBound;

    public RandomNumber(int lb, int ub){
        lowerBound = lb;
        upperBound = ub;
    }

    public int randomInt(double x1, double y1, double x2, double y2){
        // int[] nums = {(int) x1, (int) y1, (int) x2, (int) y2};
        // int seed = ( Math.max(Math.max(nums[0], nums[1]) , Math.max(nums[2], nums[3]) ) * Math.min(Math.min(nums[0],nums[1]),Math.min(nums[2],nums[3])) ) % (nums[0]+nums[1]+nums[2]+nums[3]+1);
        // int randNum = seed%(upperBound-lowerBound) + lowerBound;
        // return randNum;
    }
}