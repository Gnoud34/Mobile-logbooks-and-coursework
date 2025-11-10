import 'package:flutter/material.dart';
import '../db/database_helper.dart';
import '../models/hike.dart';
import 'add_hike_screen.dart';
import 'hike_detail.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  List<Hike> _hikes = [];
  String _query = '';

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    final rows = await DatabaseHelper.instance.query('hikes', orderBy: 'date DESC');
    setState(() => _hikes = rows.map(Hike.fromMap).toList());
  }

  Future<void> _search(String text) async {
    _query = text;
    if (text.isEmpty) return _load();
    final rows = await DatabaseHelper.instance.query(
      'hikes',
      where: 'name LIKE ?',
      whereArgs: ['%$text%'],
      orderBy: 'date DESC',
    );
    setState(() => _hikes = rows.map(Hike.fromMap).toList());
  }

  Future<void> _resetDb() async {
    // await DatabaseHelper.instance.deleteAll('observations');
    await DatabaseHelper.instance.deleteAll('hikes');
    _load();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('M-Hike'),
        actions: [
          IconButton(
            tooltip: 'Reset database',
            onPressed: _resetDb,
            icon: const Icon(Icons.delete_forever),
          )
        ],
        bottom: PreferredSize(
          preferredSize: const Size.fromHeight(56),
          child: Padding(
            padding: const EdgeInsets.all(8),
            child: TextField(
              decoration: const InputDecoration(
                hintText: 'Search by name…',
                prefixIcon: Icon(Icons.search),
                border: OutlineInputBorder(),
              ),
              onChanged: _search,
            ),
          ),
        ),
      ),
      body: ListView.separated(
        itemCount: _hikes.length,
        separatorBuilder: (_, __) => const Divider(height: 1),
        itemBuilder: (_, i) {
          final h = _hikes[i];
          return ListTile(
            title: Text(h.name),
            subtitle: Text('${h.location} • ${h.date} • ${h.difficulty}'),
            trailing: IconButton(
              icon: const Icon(Icons.delete),
              onPressed: () async {
                await DatabaseHelper.instance.deleteById('hikes', h.id!);
                // await DatabaseHelper.instance.deleteAll('observations'); // dọn orphan nếu có
                _load();
              },
            ),
            onTap: () async {
              await Navigator.push(context,
                  MaterialPageRoute(builder: (_) => HikeDetailScreen(hike: h)));
              _load();
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          await Navigator.push(context,
              MaterialPageRoute(builder: (_) => const AddHikeScreen()));
          _load();
        },
        icon: const Icon(Icons.add),
        label: const Text('Add Hike'),
      ),
    );
  }
}
