import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List
import Data.Typeable
import Data.Maybe

main = do
    let validPenal = ["1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1", "", ""]
    let invalidPenal = ["1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 1 1","1 1 1 1 1 1 a1 1", ""]

    let intList = convertPenalToInt validPenal
    -- This will print 8 on 'validPenal'
    -- and will print 7 on 'invalidPenal' as one row in 'invalidPenal' contains a garbage value
    putStrLn $ show (intList)


-- Returns a list of lists of type Int. 
-- Removes a row of penalties if it is of an invalid form.
-- Therefore to check for machine penalty validity just check that this
-- resulting list of lists is of length 8. If length 8, then all penalties are valid
convertPenalToInt :: [String] -> [[Int]]
convertPenalToInt [] = []
convertPenalToInt (x:xs)
    |(machinePenalRowValid y) && (xs /= []) && (length y == 8) = stringListToIntList y: convertPenalToInt xs
    |(machinePenalRowValid y) && (xs == []) && (length y == 8) = stringListToIntList y:convertPenalToInt xs
    |otherwise = convertPenalToInt (xs)
    where y = stringToStringList x

-- Converts [String] to [Int]
stringListToIntList :: [String] -> [Int]
stringListToIntList [] = []
stringListToIntList (x:xs) = fromMaybe (-1) y:stringListToIntList xs
    where y = readMaybe x :: Maybe Int

-- Converts String to [String]
stringToStringList :: String -> [String]
stringToStringList "" = []
stringToStringList str = words(str)
    
-- Converts all string indices in string array to Ints and checks that they are >= 0 and natural numbers.
-- Returns true if whole row is valid
-- False if at least one is invalid
machinePenalRowValid :: [String] -> Bool
machinePenalRowValid [] = True
machinePenalRowValid (x:xs)    
    |(y /= Nothing ) && (fromMaybe (0) y >= 0) && (fromMaybe (0) y `mod` 1 == 0) && (xs /= []) = machinePenalRowValid xs
    |(y /= Nothing) && (xs == []) = True
    |(y == Nothing) = False
    |otherwise = False
    where y = readMaybe x :: Maybe Int

-- Takes a string and either returns a Just type of that value or Nothing.
readMaybe :: Read a=> String -> Maybe a
readMaybe s = case reads s of
                [(val, "")] -> Just val
                _           -> Nothing

