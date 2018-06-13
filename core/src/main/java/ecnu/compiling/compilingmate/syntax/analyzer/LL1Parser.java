package ecnu.compiling.compilingmate.syntax.analyzer;

import java.util.*;

public class LL1Parser {
    //产生式
    private Map<String,List<String>> grammar;
    //字表
    private List<String> terminalDict;
    private List<String> nontermDict;
    private Map<String,Set<String>> First;
    private Map<String,Set<String>> Follow;
    private String emptySymbol;
    private String startSymbol;
    private String borderSymbol;
    private Map<String,Map<String,Production>> analysisTable;
    private String delimeter=" ";


    public List<String> getRowIndex(){
        return nontermDict;
    }
    public List<String> getColIndex(){
        List<String> rtn=new ArrayList<>(terminalDict);
        rtn.add(borderSymbol);
        return rtn;
    }
    //去掉连续空格以及头尾空格
    private String strip(String str){
        while(str.contains("  "))
            str=str.replace("  "," ");
        return str.trim();
    }
    public List<String> getNonterminals(){
        return nontermDict;
    }
    public List<String> getTerminals(){
        return terminalDict;
    }
    public Map<String,List<String>> getFirstSet(){
        Map<String,List<String>> rtn=new HashMap<>();
        for(String key:First.keySet()){
            Set<String> value=First.get(key);
            List<String> valueList=new ArrayList<>(value);
            rtn.put(key,valueList);
        }
        return rtn;
    }
    public Map<String,List<String>> getFollowSet(){
        Map<String,List<String>> rtn=new HashMap<>();
        for(String key:Follow.keySet()){
            Set<String> value=Follow.get(key);
            List<String> valueList=new ArrayList<>(value);
            rtn.put(key,valueList);
        }
        return rtn;
    }
    public List<List<Map<String,Object>>> getParseTable(){
        List<List<Map<String,Object>>> rst= new ArrayList<>();
        List<String> rowIndex=getRowIndex();
        List<String> colIndex=getColIndex();
        for(String row:rowIndex){
            List<Map<String,Object>> line=new ArrayList<>();
            for(String col:colIndex){
                Production p =analysisTable.get(row).get(col);
                line.add(formJsonData(p));
            }
            rst.add(line);
        }
        return rst;
    }

    private Map<String,Object> formJsonData(Production p) {
        Map<String,Object> rtn=new HashMap<>();
        if(p!=null) {
            Map<String, String> map1 = new HashMap<>();
            map1.put("left", p.getLeft());
            map1.put("right", p.getCandidates().get(0));
            rtn.put("production", map1);
            map1 = new HashMap<>();
            map1.put("type", p.getType());
            map1.put("key", p.getKey());
            rtn.put("reason", map1);
        }
        return rtn;
    }

    //非LL1型文法抛出错误
    public LL1Parser(String startSymbol, List<Map<String,String>> productions) throws Exception{
        grammar=new HashMap<String, List<String>>();
        this.startSymbol=startSymbol;
        emptySymbol="ε";
        borderSymbol="#";
        nontermDict=new ArrayList<>();
        terminalDict=new ArrayList<>();
        for(Map<String,String> p:productions){
            String left=strip(p.get("left"));
            String right=strip(p.get("right"));
            if(!grammar.containsKey(left))
                grammar.put(left,new ArrayList<String>(Arrays.asList((right))));
            else
                grammar.get(left).add(right);
            if(!this.nontermDict.contains(left)){
                nontermDict.add(left);
            }
        }
        for(Map<String,String> p:productions){
            String right=strip(p.get("right"));
            String[] rightList=getSymbolList(right);
            for(String str:rightList){
                if(!nontermDict.contains(str) && !terminalDict.contains(str) && !str.equals(emptySymbol))
                    terminalDict.add(str);
            }
        }

//        grammar.put("E",new ArrayList<String>(Arrays.asList("TR")));
//        grammar.put("R",new ArrayList<String>(Arrays.asList("+TR","ε")));
//        grammar.put("T",new ArrayList<String>(Arrays.asList("FY")));
//        grammar.put("Y",new ArrayList<String>(Arrays.asList("*FY","ε")));
//        grammar.put("F",new ArrayList<String>(Arrays.asList("(E)","i")));
//        terminalDict=new ArrayList<String>(){{addAll(Arrays.asList("+","(",")","i","*"));}};
//        nontermDict =new ArrayList<String>(){{addAll(Arrays.asList("E","T","R","F","Y"));}};
//        startSymbol="E";
//        emptySymbol="ε";
//        borderSymbol="#";
        initFirst();
        initFollow();
        System.out.println("终结符："+terminalDict.toString());
        System.out.println("非终结符："+nontermDict.toString());
        System.out.println("First集："+First.toString());
        System.out.println("Follow集："+Follow.toString());
        buildAnalysisTable();
    }

    //demo string: i+i*i
    public boolean analysis(String input){
        int size=input.length();
        if(!input.substring(size-1,size).equals(borderSymbol)){
            input+=borderSymbol;
        }
        Stack<String> stack=new Stack<>();
        stack.push(borderSymbol);
        stack.push(startSymbol);
        String[] chList=getSymbolList(input);
        int index=0;
        while(true){
            String curSym=chList[index];
            Production p;
            try {
                p = analysisTable.get(stack.peek()).get(curSym);
            }catch (Exception e){return false;}
            print(stack,chList,index,p);
            stack.pop();
            if(p==null)
                return false;
            String candidate=p.getCandidates().get(0);
            String[] candiSymList=getSymbolList(candidate);

            for(int i=candiSymList.length-1;i>=0;i--){
                if(!candiSymList[i].equals(emptySymbol))
                    stack.push(candiSymList[i]);
            }
            if(stack.peek().equals(curSym)){
                index++;
                stack.pop();
            }
            if(stack.size()==0 && index==chList.length)
                return true;
        }
    }

    private void print(Stack<String> stack, String[] chList, int index, Production p) {
        Stack<String> mStack=(Stack<String>)stack.clone();
        List<String> list=new ArrayList<>();
        while(mStack.size()>0){
            list.add(mStack.pop());
        }
        for(int i=list.size()-1;i>=0;i--){
            System.out.print(list.get(i));
        }
        System.out.print("\t");
        for(int i=index;i<chList.length;i++){
            System.out.print((chList[i]));
        }
        System.out.print("\t");
        System.out.print(p.getLeft()+"→"+p.getCandidates().get(0)+"\n");
    }

    //return 是否为LL1文法
    private void buildAnalysisTable() throws Exception{
        analysisTable=new HashMap<>();
        for(String left:nontermDict){
            analysisTable.put(left,new HashMap<>());
            for(String symbol:terminalDict){
                analysisTable.get(left).put(symbol,null);
            }
            analysisTable.get(left).put(borderSymbol,null);
        }
        for(String left:grammar.keySet()){
            List<String> candidates=grammar.get(left);
            for(String candidate:candidates){
                Set<String> tFirst=First.get(candidate);
                for(String symbol:tFirst){
                    if(isTerminal(symbol)) {
                        if(analysisTable.get(left).get(symbol)!=null)
                            throw new Exception("Invalid grammar for LL1");
                        else
                            analysisTable.get(left).put(symbol, new Production(left, Arrays.asList(candidate),"first",candidate));
                    }
                }
                if(tFirst.contains(emptySymbol)){
                    Set<String> tFollow=Follow.get(left);
                    for(String symbol:tFollow){
                        if(analysisTable.get(left).get(symbol)!=null)
                            throw new Exception("Invalid grammar for LL1");
                        else
                            analysisTable.get(left).put(symbol,new Production(left,Arrays.asList(candidate),"follow",left));
                    }
                }
            }
        }
        for(String row: nontermDict){
            List<String> tmp=new ArrayList<>(terminalDict);
            tmp.add(borderSymbol);
            for(String col:tmp){
                Production p=analysisTable.get(row).get(col);
                if(p!=null)
                    System.out.print(p.getLeft()+"→"+p.getCandidates().toString()+"\t\t");
                else
                    System.out.print("NULL"+"\t\t");
            }
            System.out.print("\n");
        }
    }



    private void initFollow() {
        Follow=new HashMap<>();
        for(String left:grammar.keySet()){
            Follow.put(left,new HashSet<>());
            if(left.equals(startSymbol))
                Follow.get(left).add(borderSymbol);
        }
        boolean addNew=true;
        while(addNew){
            addNew=false;
            int oldCount=getValueCount(Follow);
            for(String left:grammar.keySet()){
                List<String> candidates=grammar.get(left);
                for(String candidate:candidates){
                    String[] chList=getSymbolList(candidate);
                    for(int i=0;i<chList.length;i++){
                        if(isNonterminal(chList[i])){
                            if(i<chList.length-1) {
                                Follow.get(chList[i]).addAll(stripEmpty(First.get(chList[i + 1])));
                                String next=String.join(delimeter,new ArrayList<>(Arrays.asList(chList)).subList(i+1,chList.length));
                                if(First.containsKey(next) && First.get(next).contains(emptySymbol)){
                                    if(Follow.containsKey(left))
                                        Follow.get(chList[i]).addAll(Follow.get(left));
                                }
                            }
                            else{
                                if(Follow.containsKey(left))
                                    Follow.get(chList[i]).addAll(Follow.get(left));
                            }
                        }
                    }
                }
            }
            int newCount=getValueCount(Follow);
            if(newCount!=oldCount)addNew=true;
        }
    }

    private boolean isNonterminal(String s) {
        return nontermDict.contains(s);
    }

    private Set<String> stripEmpty(Set<String> strings) {
        Set<String> tmp=new HashSet(strings);
        tmp.remove(emptySymbol);
        return tmp;
    }

    //得到所有value的length之和
    private int getValueCount(Map<String, Set<String>> follow) {
        int count=0;
        for(String key:follow.keySet()){
            count+=follow.get(key).size();
        }
        return count;
    }

    private void initFirst() {
        First=new HashMap<>();
        //初始化
        for(String s: nontermDict){
            First.put(s,new HashSet<>());
            for(String candidate:grammar.get(s)){
                First.put(candidate,new HashSet<>());
            }
        }
        for(String s:terminalDict){
            First.put(s,new HashSet<String>(){{add(s);}});
        }
        //对每个非终结符构造first集
        boolean addNew=true;
        while(addNew){
            addNew=false;
            for(String key:grammar.keySet()){
                List<String> value=grammar.get(key);
                //每一个候选产生式
                for(String s:value){
                    //该产生式的符号列表
                    String[] chList=getSymbolList(s);
                    String firstSymbol=chList[0];
                    if(isTerminal(firstSymbol) || isEmptySymbol(s)){
                        if(!First.get(key).contains(firstSymbol)) {
                            First.get(key).add(firstSymbol);
                            addNew=true;
                        }
                    }
                    else if(isNonterminal(firstSymbol)) {
                        for (String s1 : First.get(firstSymbol)) {
                            if (!s1.equals(emptySymbol) && !First.get(key).contains(s1)) {
                                First.get(key).add(s1);
                                addNew = true;
                            }
                        }
                        int i = 0;
                        while ( i < chList.length && isNonterminal(chList[i]) && First.get(chList[i]).contains(emptySymbol) ) {
                            i++;
                        }
                        if (i < chList.length) {
                            for (String s1 : First.get(chList[i])) {
                                if (!s1.equals(emptySymbol) && !First.get(key).contains(s1)) {
                                    First.get(key).add(s1);
                                    addNew = true;
                                }
                            }
                        } else {
                            if (!First.get(key).contains(emptySymbol)) {
                                First.get(key).add(emptySymbol);
                                addNew = true;
                            }
                        }
                    }
                }
            }
        }
        //对非终结符的所有候选构造first集
        for(String left:grammar.keySet()){
            List<String> candidates=grammar.get(left);
            for(String candidate:candidates){
                //首先加入第一个字符的first集
                String[] chList=getSymbolList(candidate);
                First.put(candidate,stripEmpty(First.get(chList[0])));
                //找第一个first集不含空的
                int i = 0;
                while (  i < chList.length && First.get(chList[i]).contains(emptySymbol)) {
                    i++;
                }
                if(i<chList.length){
                    First.get(candidate).addAll(stripEmpty(First.get(chList[i])));
                }
                else{
                    First.get(candidate).add(emptySymbol);
                }
            }
        }
        //空
        First.put(emptySymbol,new HashSet<>(Arrays.asList(emptySymbol)));
    }


    private boolean isTerminal(String s){
        return terminalDict.contains(s);
    }

    private String[] getSymbolList(String s) {
        return s.split(delimeter);
    }

    private String getFirstSymbol(String s) {
        return s.substring(0,1);
    }

    private boolean isEmptySymbol(String s){
        return s.equals(emptySymbol);
    }

    //放在解析表格子中
    class Production{
        private String left;
        private List<String> candidates;
        //存储由谁推导出的该产生式
        private String type;//"first" or "follow"
        private String key;//first or follow 集的key

        public Production(String left, List<String> candidates, String type,String key) {
            this.left = left;
            this.candidates = candidates;
            this.type=type;
            this.key=key;
        }

        public String getLeft() {
            return left;
        }

        public List<String> getCandidates() {
            return candidates;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getType() {
            return type;
        }

        public String getKey() {
            return key;
        }
    }

//    public static void main(String[] args) {
//        try {
//            LL1 ll1 = new LL1();
//            ll1.analysis("i+i*i");
//        }catch (Exception e){e.printStackTrace();}
//    }
}

