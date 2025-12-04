import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class DatabaseHelper {
  DatabaseHelper._();
  static final DatabaseHelper instance = DatabaseHelper._();
  static Database? _db;

  Future<Database> get db async {
    if (_db != null) return _db!;
    final dir = await getDatabasesPath();
    final path = join(dir, 'mhike.db');
    _db = await openDatabase(
      path,
      version: 4,
      onCreate: _onCreate,
      onUpgrade: _onUpgrade,
    );
    return _db!;
  }

  Future _onCreate(Database db, int version) async {
    await db.execute('''
      CREATE TABLE hikes(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        location TEXT NOT NULL,
        date TEXT NOT NULL,
        parking TEXT NOT NULL,
        length TEXT NOT NULL,
        difficulty TEXT NOT NULL,
        description TEXT,
        weather TEXT,
        terrain TEXT
      )
    ''');

    await db.execute('''
      CREATE TABLE observations(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        hikeId INTEGER NOT NULL,
        observation TEXT NOT NULL,
        time TEXT NOT NULL,
        comment TEXT,
        FOREIGN KEY(hikeId) REFERENCES hikes(id) ON DELETE CASCADE
      )
    ''');
  }

  Future _onUpgrade(Database db, int oldVersion, int newVersion) async {
    if (oldVersion < 2) {
      await db.execute('ALTER TABLE hikes ADD COLUMN terrain TEXT');
    }

    if (oldVersion < 3) {
      await db.execute('''
        CREATE TABLE IF NOT EXISTS observations(
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          hikeId INTEGER NOT NULL,
          observation TEXT NOT NULL,
          time TEXT NOT NULL,
          comment TEXT,
          FOREIGN KEY(hikeId) REFERENCES hikes(id) ON DELETE CASCADE
        )
      ''');
    }
  }

  Future<int> insert(String table, Map<String, dynamic> data) async {
    final d = await db;
    return d.insert(table, data, conflictAlgorithm: ConflictAlgorithm.replace);
  }

  Future<List<Map<String, dynamic>>> query(String table,
      {String? where, List<Object?>? whereArgs, String? orderBy}) async {
    final d = await db;
    return d.query(table, where: where, whereArgs: whereArgs, orderBy: orderBy);
  }

  Future<int> update(String table, Map<String, dynamic> data, int id) async {
    final d = await db;
    return d.update(table, data, where: 'id=?', whereArgs: [id]);
  }

  Future<int> deleteById(String table, int id) async {
    final d = await db;
    return d.delete(table, where: 'id=?', whereArgs: [id]);
  }

  Future<int> deleteAll(String table) async {
    final d = await db;
    return d.delete(table);
  }

  Future<List<Map<String, dynamic>>> raw(String sql, [List<Object?>? args]) async {
    final d = await db;
    return d.rawQuery(sql, args);
  }
}
