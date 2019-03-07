import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List
import Data.Typeable
import Data.Maybe


-- Chop input.txt file into sublists of data between headers
slice :: Int -> Int -> [String] -> [String]

slice from to xs = take (to - from + 1) (drop from xs)
 



-- Main code execution, where all header checks happen
main = do
    args <- getArgs
    -- If CLI arguments are in wrong format exit
    if  length (args) /= 2 then do
        putStrLn("usage: <input file> <output file>")
        exitFailure
    else do
        content <- readFile (head args)
        -- Put input.txt (arg[0]) into [String]
        let text = lines content

        -- Get indices of all headers by converting to Int from Maybe Int. 
        -- If header specified as below is not in the input.txt file, return -1 as fromMaybe
        -- function cannot convert the index if it does not exist
        let nameInd = fromMaybe (-1) (elemIndex "Name:" text)
        let fpaInd = fromMaybe (-1) (elemIndex "forced partial assignment:" text)
        let fmInd = fromMaybe (-1) (elemIndex "forbidden machine:" text)
        let tntInd = fromMaybe (-1) (elemIndex "too-near tasks:" text)
        let mpInd = fromMaybe (-1) (elemIndex "machine penalties:" text)
        let tnpInd = fromMaybe (-1) (elemIndex "too-near penalities" text)


        -- Measure length of input.txt String list. This is to be used to find last index of list.
        let endOflist = length text

    	-- Add all header indices to their own list
        let headerIndices = nameInd:fpaInd:fmInd:tntInd:mpInd:tnpInd:[]
        -- If -1 is not present in headerIndices list, headers are of valid format
        let valid  = not (-1 `elem` headerIndices)

        -- If headers valid, splice input.txt on each subsection of data inbetween header indices.
        if valid then do
            let nameList = slice (nameInd+1) (fpaInd-1) text
            let fpaList = slice (fpaInd+1) (fmInd-1) text
            let fmList = slice (fmInd+1) (tntInd-1) text
            let tntList = slice (tntInd+1) (mpInd-1) text
            let mpList = slice (mpInd+1) (tnpInd-1) text
            let tnpList = slice (tnpInd+1) ((endOflist-1)) text
            -- Print all sublists
            putStrLn $ show (nameList)
            putStrLn $ show (fpaList)
            putStrLn $ show (fmList)
            putStrLn $ show (tntList)
            putStrLn $ show (mpList)
            putStrLn $ show (tnpList)

        -- Else file format is invalid, and we write "Error while parsing to output.txt"
        else do
            file_out <- writeToFile (last args) valid
            exitFailure
        return 0

-- Write to file routine that gets executed if headers are in wrong format.
writeToFile file_name valid = do
    fileToWrite <- openFile file_name WriteMode
    if valid then
        hPutStrLn fileToWrite("")
    else 
    	hPutStrLn fileToWrite ("Error while parsing")
    hClose fileToWrite