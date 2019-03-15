import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List
import Data.Typeable
import Data.Maybe

headers = ["Name:", "forced partial assignment:", "forbidden machine:", "too-near tasks:", "machine penalties:", "too-near penalities"]

-- An Example main do block showing how to utilize defined functions below
main = do
    args <- getArgs
    -- If CLI arguments are in wring format exit
    if length(args) /= 2 then do
        putStrLn("usage: <input file> <output file>")
        exitFailure
    else do
        content <- readFile (head args)
        -- Put input.txt (arg[0]) into [String]
        let headers = ["Name:", "forced partial assignment:", "forbidden machine:", "too-near tasks:", "machine penalties:", "too-near penalities"]

        let text = lines content
        let headerIndices = headerIndexList headers text 
        let listOfSubsections = getSubsections headerIndices text
        -- Example access of name list
        putStrLn $ show (listOfSubsections!!0)
        -- Example access of forced partial assignments list
        putStrLn $ show (listOfSubsections!!1)
        -- Example access of forbidden machines list
        putStrLn $ show (listOfSubsections!!2)
        -- Example access of too-near tasks list
        putStrLn $ show (listOfSubsections!!3)
		-- Example access of machine penalties list
        putStrLn $ show (listOfSubsections!!4)
		-- Example access of too-near penalities list
        putStrLn $ show (listOfSubsections!!5)


-- Chop input.txt file into sublists of data between headers
slice :: Int -> Int -> [String] -> [String]
slice from to xs = take (to - from + 1) (drop from xs)

-- Finds index of all headers in input file string list and append them to a list of their own
-- If the header does not exist in the input file string list then append -1 to the resulting list
headerIndexList :: [String] -> [String] -> [Int]
headerIndexList [] inputFileList = []
headerIndexList (x:xs) inputFileList = (fromMaybe (-1) (elemIndex x inputFileList)):headerIndexList xs inputFileList

-- If -1 is not present in list of header indices then the headers are of a valid form
headerValidity :: [Int] -> Bool
headerValidity headerIndices = not (-1 `elem` headerIndices)

-- Intakes list of header indices and input string list
-- Goes through each index and sections off the strings between header indices into their own lists.
-- These individual lists of strings are appended to a master list containing all input file data.
getSubsections :: [Int] -> [String] -> [[String]]
getSubsections [] inputFileList = []
getSubsections (x:xs) inputFileList 
    | (xs /= []) = slice (x+1) ((head xs)-1) inputFileList:getSubsections xs inputFileList
    | otherwise = slice (x+1) ((length inputFileList)-1) inputFileList:getSubsections xs inputFileList


