package restaruantSimulation;

public interface Food 
{
	enum MainCourse implements Food
	{
		LASAGNE,BURRITO,PAD_THAI,LENTILS,HUMMOUS,VINDALOO;
	}
	enum Appetizer implements Food
	{
		SALAD,SOUP,SPRING_ROLLS;
	}
	
	enum Dessert implements Food
	{
		TIRAMISU,GELATO,BALCK_FORSET_CAKE,FRUIT,CREME_CARAMEL;
	}
	enum Coffee implements Food
	{
		BLACK_COFFEE,DECAF_COFFEE,ESPRESSO,LATTE,CAPPUCCINO,TEA,HERB_TEA;
	}
}
