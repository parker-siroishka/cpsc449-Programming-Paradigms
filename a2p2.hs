import Data.List
import Data.Char
import System.IO
import Control.Monad
import Data.List
import Data.List.Split

--when :: Applicative f => Bool -> f() -> f()
a = ["wrongtooneartasks", ""]
b = ["(1,A)","(2,B)", "(3,C)", "(4,D)", "(5,E)",""]
c = ["(1,A)", ""]
d = ["(A,B)"]
e = ["1 1 1 1 1 1 1 1", "1 1 1 1 1 1 1 1", "1 1 1 1 1 1 1 1", "1 1 1 1 1 1 1 1" ,"1 1 1 1 1 1 1 1" , "1 1 1 1 1 1 1 1", "1 1 1 1 1 1 1 1", "1 1 1 1 1 1 1 1"]
f = ["(A,C,7)", "(B,X,3)"]


-- step 1. Check if a contains empty 
-- if length == 1, check to make sure a is not empty 
-- if length == 2, check to make sure a is not 

len_a = length a 

statement_1 = "length is two"

-- filename check 
two_outcome = if (len_a == 2) 
	then  
		if (a !! 0  /= "") && (a !! 1 == "")  
			then putStrLn "Valid"
			else putStrLn "Error: length two invalid name"
	else if (len_a > 2) 
		then putStrLn $ "Error: name is not valid, too many names"
	else if (len_a == 1)
		then 
			if a !! 0 /= ""
				then putStrLn $ "Length one and valid"
				else putStrLn $ "Error: Length one and invalid"
	else
		putStrLn $ "Error: unaccounted for error?"

-- now need to make sure b is valid 

legal_machines = ['1'.. '8']
legal_tasks = ['A' .. 'H']


-- need to make sure all forced partial assignments are valid 
parse new_tup =  new_tup !! 0  

b2 = ["1,A)","(2,B", "(3 ,C)", "(4,,)", "(5,E)"]
b3 = ["(5,E)"]


tst =  [ validTuple x 2 | x <- b2 ]




validStruct :: String -> Bool
validStruct [] = True 
validStruct (x:xs)
	| xs /= [] && x == ' ' && (head xs) == ' ' = False 
	| (isDigit x) || x == ' ' = validStruct xs
	| otherwise = False 

-- check to make sure each tuple:
-- starts with '('
-- ends with ')'
-- is of length 5 '(?,?)'
-- there is only one comma 
-- machine is between 1 and 8 
-- task is between A and H
-- TODO: need to handle when there is a "" at the end of passed in list 
validTuple :: String -> Int -> Bool 
validTuple str len = 
	(thereAreBrackets str) && (isLen5 str) && (splitComm str == len)
	&& ((str !! 1) ` elem ` legal_machines) && ((str !! 3 ) ` elem ` legal_tasks)
--	&& (splitComm str) == len 


-- this ensures all the tuples passed in are valid 
call_tst = if False `elem` (tst)
			then "Invalid tuples mane"
			else
				"Tuples are valid mane"

nospaces [ ] = False 
nospaces (' ': xs) = False
nospaces (x:xs) = nospaces xs 

isLen5 :: String -> Bool 
isLen5 str = length (str) == 5

thereAreBrackets [ ] = False 
thereAreBrackets str = (head str) == '(' && (last str) == ')'

splitComm :: String -> Int
splitComm str = length (splitOn "," str)




--- Still need to check the weights for machine task pairs 



--- need to check too near 


--- need to figure out how to write to file?

